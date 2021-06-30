object Versions {
    const val kotlin = "1.5.10"
    const val kotlinCoroutines = "1.5.0-native-mt"
    const val ktor = "1.6.0"
    const val kotlinxSerialization = "1.2.1"
    const val koin = "3.1.0"
}

object Koin {
    val core = "io.insert-koin:koin-core:${Versions.koin}"
    val test = "io.insert-koin:koin-test:${Versions.koin}"
    val android = "io.insert-koin:koin-android:${Versions.koin}"
}

object Serialization {
    val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
}

object Ktor {
    val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
    val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
    val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"

    val clientAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    val clientIos = "io.ktor:ktor-client-ios:${Versions.ktor}"
}