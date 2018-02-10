package swapp.items.com.swappify.common

import com.google.firebase.firestore.GeoPoint

class LocationHelper {

    companion object {
        const val SOUTH_WEST = 1
        const val NORTH_EAST = 2
    }


    public fun boundingBoxCoordinates(geoPoint: GeoPoint, radius: Int, type: Int): GeoPoint {
        val KM_PER_DEGREE_LATITUDE = 110.574
        val latDegrees = radius / KM_PER_DEGREE_LATITUDE
        val latitudeNorth = Math.min(90.0, geoPoint.latitude + latDegrees)
        val latitudeSouth = Math.max(-90.0, geoPoint.latitude - latDegrees)
        // calculate longitude based on current latitude
        val longDegsNorth = metersToLongitudeDegrees(radius, latitudeNorth)
        val longDegsSouth = metersToLongitudeDegrees(radius, latitudeSouth)
        val longDegs = Math.max(longDegsNorth, longDegsSouth)

        if (type == SOUTH_WEST) {
            return GeoPoint(latitudeSouth, wrapLongitude(geoPoint.longitude - longDegs))
        } else {
            return GeoPoint(latitudeNorth, wrapLongitude(geoPoint.longitude + longDegs))
        }
    }


    private fun metersToLongitudeDegrees(distance: Int, latitude: Double): Double {
        val EARTH_EQ_RADIUS = 6378137.0
        // this is a super, fancy magic number that the GeoFire lib can explain (maybe)
        val E2 = 0.00669447819799
        val EPSILON = 1e-12
        val radians = toRadians(latitude)
        val num = Math.cos(radians) * EARTH_EQ_RADIUS * Math.PI / 180
        val denom = 1 / Math.sqrt(1 - E2 * Math.sin(radians) * Math.sin(radians))
        val deltaDeg = num * denom
        if (deltaDeg < EPSILON) {
            return if (distance > 0 ) 360.0 else 0.0
        }
        return Math.min(360.0, distance / deltaDeg)
    }

    private fun wrapLongitude(longitude: Double): Double {
        if (longitude <= 180 && longitude >= -180) {
            return longitude
        }
        val adjusted = longitude + 180
        if (adjusted > 0) {
            return (adjusted % 360) - 180
        }
        return 180 - (-adjusted % 360)
    }


    fun distance(geoPoint1: GeoPoint, geoPoint2: GeoPoint): Double {
        val radius = 6371 // Earth's radius in kilometers
        val latDelta = toRadians(geoPoint2.latitude - geoPoint2.latitude);
        val lonDelta = toRadians(geoPoint2.longitude - geoPoint2.longitude);

        val diffDelta = (Math.sin(latDelta / 2) * Math.sin(latDelta / 2)) +
                (Math.cos(toRadians(geoPoint1.latitude)) * Math.cos(toRadians(geoPoint2.latitude)) *
                        Math.sin(lonDelta / 2) * Math.sin(lonDelta / 2))

        val c = 2 * Math.atan2(Math.sqrt(diffDelta), Math.sqrt(1 - diffDelta))

        return radius * c
    }

    private fun toRadians (angle: Double): Double = angle * (Math.PI / 180)
}
