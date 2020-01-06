package com.differ.differcore.models

/**
 * Result of methods which can end with some error.
 *
 * @property T the type of success return data from service.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
sealed class Either<out T : Any> {
    /**
     * Result of success ends methods.
     *
     * @param value data from method to caller.
     */
    data class Success<out T : Any>(val value: T) : Either<T>()

    /**
     * Result of error ends methods.
     *
     * @param message message for caller which describe what exactly went wrong;
     * @param cause if there was an exception during method execution. May be null if there is no exception.
     */
    data class Error(val message: String, val cause: Exception? = null) : Either<Nothing>()
}