package com.dleite.stockmarketapp.data.mapper

import com.dleite.stockmarketapp.data.local.CompanyListingEntity
import com.dleite.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.dleite.stockmarketapp.domain.model.CompanyInfo
import com.dleite.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListing(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}