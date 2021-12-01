package com.saltserv.assessment.responses

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
class SearchResponse(
    val artists: Artists
)

@Serializable
class Artists(
    val href: String,
    val items: List<ArtistItem>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
)

@Serializable
class ArtistItem(
    val external_urls: ExternalUrls,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ExternalUrls::class.java.classLoader)!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Image)!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(external_urls, flags)
        parcel.writeStringList(genres)
        parcel.writeString(href)
        parcel.writeString(id)
        parcel.writeTypedList(images)
        parcel.writeString(name)
        parcel.writeInt(popularity)
        parcel.writeString(type)
        parcel.writeString(uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArtistItem> {
        override fun createFromParcel(parcel: Parcel): ArtistItem {
            return ArtistItem(parcel)
        }

        override fun newArray(size: Int): Array<ArtistItem?> {
            return arrayOfNulls(size)
        }
    }
}