package com.wisesoda.data.cache;

import android.content.Context;

import com.google.gson.JsonSerializer;
import com.wisesoda.data.entity.BlogEntity;
import com.wisesoda.domain.executor.ThreadExecutor;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by douncoding on 16. 8. 8..
 */
public class BlogCacheImpl implements BlogCache {

    private static final String SETTINGS_FILE_NAME = "com.fernandocejas.android10.SETTINGS";
    private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";

    private static final String DEFAULT_FILE_NAME = "user_";
    private static final long EXPIRATION_TIME = 60 * 10 * 1000;

    private final Context context;
    private final File cacheDir;
    private final JsonSerializer serializer;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    @Inject
    public BlogCacheImpl(Context context, JsonSerializer serializer, FileManager fileManager,
                         ThreadExecutor executor) {
        this.context = context;
        this.cacheDir = this.context.getCacheDir();
        this.serializer = serializer;
        this.fileManager =fileManager;
        this.threadExecutor = executor;
    }

    @Override
    public Observable<BlogEntity> getList() {
        return null;
    }

    @Override
    public void put(List<BlogEntity> blogEntities) {
        if (blogEntities != null) {

        }
    }

    @Override
    public boolean isCached(int userId) {
        return false;
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            this.evictAll();
        }

        return expired;
    }

    @Override
    public void evictAll() {
        this.executeAsynchronously(new CacheEvictor(this.fileManager, this.cacheDir));
    }

    /**
     * Build a file, used to be inserted in the disk cache.
     *
     * @param userId The id user to build the file.
     * @return A valid file.
     */
    private File buildFile(int userId) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(DEFAULT_FILE_NAME);
        fileNameBuilder.append(userId);

        return new File(fileNameBuilder.toString());
    }

    /**
     * Set in millis, the last time the cache was accessed.
     */
    private void setLastCacheUpdateTimeMillis() {
        long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    private long getLastCacheUpdateTimeMillis() {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    /**
     * Executes a {@link Runnable} in another Thread.
     *
     * @param runnable {@link Runnable} to execute
     */
    private void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    private static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWriter;
        private final String fileContent;

        CacheWriter(FileManager fileManager, File fileToWriter, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWriter = fileToWriter;
            this.fileContent = fileContent;
        }

        @Override
        public void run() {
            this.fileManager.writeToFile(fileToWriter, fileContent);
        }
    }

    private static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File cacheDir;

        CacheEvictor(FileManager fileManager, File cacheDir) {
            this.fileManager = fileManager;
            this.cacheDir = cacheDir;
        }

        @Override
        public void run() {
            this.fileManager.clearDirectory(this.cacheDir);
        }
    }
}
