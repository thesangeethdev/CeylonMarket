package com.sangeeth.ceylonmarket

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform