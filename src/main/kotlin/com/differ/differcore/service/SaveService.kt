package com.differ.differcore.service

/**
 * Service for saving current description of API.
 *
 * @see SaveServiceImpl saves API description in local file system.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
interface SaveService {

    /**
     * Should save current description of API somewhere.
     */
    fun save()
}