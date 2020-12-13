package com.shauri.fakegps.data

import android.os.Parcel
import android.os.Parcelable
import com.huawei.hms.maps.model.LatLng
import java.io.Serializable

data class ServiceData(
    val point: LatLng?,
    val mockMove: Boolean = false,
    val randomMove: Boolean = false,
    val direction: Double? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(LatLng::class.java.classLoader),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(point, flags)
        parcel.writeByte(if (mockMove) 1 else 0)
        parcel.writeByte(if (randomMove) 1 else 0)
        parcel.writeValue(direction)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServiceData> {
        override fun createFromParcel(parcel: Parcel): ServiceData {
            return ServiceData(parcel)
        }

        override fun newArray(size: Int): Array<ServiceData?> {
            return arrayOfNulls(size)
        }
    }
}