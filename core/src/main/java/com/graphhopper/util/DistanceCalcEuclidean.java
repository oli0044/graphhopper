/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util;

import com.graphhopper.api.util.DistanceCalcEarth;
import static java.lang.Math.sqrt;

import com.graphhopper.api.util.shapes.BBox;
import com.graphhopper.api.util.shapes.GHPoint;

/**
 * Calculates the distance of two points or one point and an edge in euclidean space.
 * <p>
 *
 * @author Peter Karich
 */
public class DistanceCalcEuclidean extends DistanceCalcEarth {
    @Override
    public double calcDist(double fromY, double fromX, double toY, double toX) {
        return sqrt(calcNormalizedDist(fromY, fromX, toY, toX));
    }

    @Override
    public double calcDist3D(double fromY, double fromX, double fromHeight, double toY, double toX, double toHeight) {
        return sqrt(calcNormalizedDist(fromY, fromX, toY, toX) + calcNormalizedDist(toHeight - fromHeight));
    }

    @Override
    public double calcDenormalizedDist(double normedDist) {
        return sqrt(normedDist);
    }

    /**
     * Returns the specified length in normalized meter.
     */
    @Override
    public double calcNormalizedDist(double dist) {
        return dist * dist;
    }

    double calcShrinkFactor(double a_lat_deg, double b_lat_deg) {
        return 1.;
    }

    /**
     * Calculates in normalized meter
     */
    @Override
    public double calcNormalizedDist(double fromY, double fromX, double toY, double toX) {
        double dX = fromX - toX;
        double dY = fromY - toY;
        return dX * dX + dY * dY;
    }

    @Override
    public String toString() {
        return "2D";
    }

    @Override
    public double calcCircumference(double lat) {
        throw new UnsupportedOperationException("Not supported for the 2D Euclidean space");
    }

    @Override
    public boolean isDateLineCrossOver(double lon1, double lon2) {
        throw new UnsupportedOperationException("Not supported for the 2D Euclidean space");
    }

    @Override
    public BBox createBBox(double lat, double lon, double radiusInMeter) {
        throw new UnsupportedOperationException("Not supported for the 2D Euclidean space");
    }

    @Override
    public GHPoint projectCoordinate(double latInDeg, double lonInDeg, double distanceInMeter,
            double headingClockwiseFromNorth) {
        throw new UnsupportedOperationException("Not supported for the 2D Euclidean space");
    }

    @Override
    public boolean isCrossBoundary(double lon1, double lon2) {
        throw new UnsupportedOperationException("Not supported for the 2D Euclidean space");
    }

    @Override
    public double calcNormalizedEdgeDistance(double ry, double rx,
                                             double ay, double ax,
                                             double by, double bx) {
        return calcNormalizedEdgeDistance3D(
            ry, rx, 0,
            ay, ax, 0,
            by, bx, 0
        );
    }

    @Override
    public double calcNormalizedEdgeDistance3D(double ry, double rx, double rz,
                                               double ay, double ax, double az,
                                               double by, double bx, double bz) {
        double dx = bx - ax;
        double dy = by - ay;
        double dz = bz - az;

        double norm = dx * dx + dy * dy + dz * dz;
        double factor = ((rx - ax) * dx + (ry - ay) * dy + (rz - az) * dz) / norm;

        // x,y,z is projection of r onto segment a-b
        double cx = ax + factor * dx;
        double cy = ay + factor * dy;
        double cz = az + factor * dz;

        double rdx = cx - ax;
        double rdy = cy - ay;
        double rdz = cz - az;

        return rdx * rdx + rdy * rdy + rdz * rdz;
    }
}
