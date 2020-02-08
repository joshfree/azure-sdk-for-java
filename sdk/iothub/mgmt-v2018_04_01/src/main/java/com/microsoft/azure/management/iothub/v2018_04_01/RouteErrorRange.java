/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.iothub.v2018_04_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Range of route errors.
 */
public class RouteErrorRange {
    /**
     * Start where the route error happened.
     */
    @JsonProperty(value = "start")
    private RouteErrorPosition start;

    /**
     * End where the route error happened.
     */
    @JsonProperty(value = "end")
    private RouteErrorPosition end;

    /**
     * Get start where the route error happened.
     *
     * @return the start value
     */
    public RouteErrorPosition start() {
        return this.start;
    }

    /**
     * Set start where the route error happened.
     *
     * @param start the start value to set
     * @return the RouteErrorRange object itself.
     */
    public RouteErrorRange withStart(RouteErrorPosition start) {
        this.start = start;
        return this;
    }

    /**
     * Get end where the route error happened.
     *
     * @return the end value
     */
    public RouteErrorPosition end() {
        return this.end;
    }

    /**
     * Set end where the route error happened.
     *
     * @param end the end value to set
     * @return the RouteErrorRange object itself.
     */
    public RouteErrorRange withEnd(RouteErrorPosition end) {
        this.end = end;
        return this;
    }

}