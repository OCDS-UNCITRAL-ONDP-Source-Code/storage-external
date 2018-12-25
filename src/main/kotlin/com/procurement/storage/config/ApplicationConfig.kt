package com.procurement.storage.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


@Configuration
@Import(DaoConfiguration::class, JsonConfig::class, ServiceConfig::class, WebConfig::class)
class ApplicationConfig
