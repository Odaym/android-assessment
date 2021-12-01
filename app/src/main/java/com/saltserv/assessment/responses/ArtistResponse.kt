package com.saltserv.assessment.responses

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
class ArtistResponse(
    val external_urls: ExternalUrls,
    val followers: Followers,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)

@Serializable
class Image(
    val height: Int,
    val url: String,
    val width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(height)
        parcel.writeString(url)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}

@Serializable
class Followers(
    val href: String? = null,
    val total: Int
)

@Serializable
class ExternalUrls(
    val spotify: String
) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(spotify)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExternalUrls> {
        override fun createFromParcel(parcel: Parcel): ExternalUrls {
            return ExternalUrls(parcel)
        }

        override fun newArray(size: Int): Array<ExternalUrls?> {
            return arrayOfNulls(size)
        }
    }
}