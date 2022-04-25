package com.kmm.intermediatefirstsubmission.utility

import java.util.regex.Pattern

fun String.isValidEmail(): Boolean {
    return Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                ")+"
    ).matcher(this)
        .matches()
}

fun String.limitWords(): String {
    if (this.length > 100) {
        return "${substring(1, 100)}..."
    }
    return this

}