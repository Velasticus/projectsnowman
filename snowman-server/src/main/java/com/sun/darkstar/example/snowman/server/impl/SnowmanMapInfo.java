/*
 *
 * Copyright (c) 2007-2010, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in
 *       the documentation and/or other materials provided with the
 *       distribution.
 *     * Neither the name of Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.sun.darkstar.example.snowman.server.impl;

import com.sun.darkstar.example.snowman.common.protocol.enumn.ETeamColor;
import com.sun.darkstar.example.snowman.common.util.Coordinate;
import java.util.Random;

/**
 * Static class that is used to retrieve information about snowman
 * maps.
 * 
 * @author Owen Kellett
 */
public final class SnowmanMapInfo 
{
    /**
     * Name used for the default map.
     */
    public static final String DEFAULT = "default_map";
    
    private static float[] defaultXY = new float[]{96, 96};
    private static final Random generator = new Random();
    private static final float SPAWNMULTIPLIER = 2.0f;
    private static final float SPAWNTEAMWIDTH = 10.0f;
    private static final float FLAGMULTIPLIER = 3.0f;
    
    /**
     * This class should not be instantiated
     */
    private SnowmanMapInfo() {
        
    }
    
    /**
     * Retrieve the x,y dimensions of the map with the given name.
     * 
     * @param map name of the map
     * @return array of floats representing the x,y dimensions of the map
     */
    public static float[] getDimensions(String map) {
        return defaultXY;
    }
    
    /**
     * Set the overall dimensions of the map.
     * 
     * @param x the width
     * @param y the height
     */
    public static void setDimensions(float x, float y) {
        defaultXY[0] = x;
        defaultXY[1] = y;
    }
    
    /**
     * Return the player'th spawn position for the given team on the given
     * map assuming there are totalPlayers per team.
     * This method will return positions equidistant from eachother along
     * the X axis and at a fixed Y coordinate.
     * 
     * @param map name of the map
     * @param team team to retrieve a spawn point for
     * @param player player'th player on the team (starting with 1)
     * @param teamPlayers number of players on the team
     * @return a spawn position for the player
     */
    public static Coordinate getSpawnPosition(String map,
                                              ETeamColor team,
                                              int player,
                                              int teamPlayers) {
        float[] dimensions = getDimensions(map);
        float xAxisMid = dimensions[0] / 2.0f;
        float yAxisMid = dimensions[1] / 2.0f;
        
        //set the team coordinate to be either near the top or 
        //the bottom depending on the team
        float yOffset = yAxisMid / SPAWNMULTIPLIER * (SPAWNMULTIPLIER - 1);
        Coordinate teamCoordinate = new Coordinate(xAxisMid, yAxisMid);
        switch(team) {
            case Red:
                teamCoordinate = new Coordinate(xAxisMid, yAxisMid + yOffset);
                break;
            case Blue:
                teamCoordinate = new Coordinate(xAxisMid, yAxisMid - yOffset);
                break;
            default:
                //shouldn't happen
        }
        
        //if there is only one player on the team, return the team coordinate
        //otherwise, spread the players out along a line with the team 
        //coordinate at the center
        if(teamPlayers == 1) {
            return teamCoordinate;
        }

        return new Coordinate(teamCoordinate.getX() -
                              SPAWNTEAMWIDTH / 2.0f +
                              SPAWNTEAMWIDTH / 
                              (float) (teamPlayers + 1.0f) * player,
                              teamCoordinate.getY());
    }
    
    /**
     * Return a random respawn position for the given team.
     * 
     * @param map name of the map
     * @param team team to retrieve a spawn point for
     * @return a respawn position for the player
     */
    public static Coordinate getRespawnPosition(String map,
                                                ETeamColor team) {
        return getSpawnPosition(map, 
                                team, 
                                generator.nextInt(10) + 1,
                                10);
    }
    
    /**
     * Get the start position for the given team's flag on the given map.
     * 
     * @param map the map name
     * @param team the team to check the flag position
     * @return the start position of the flag
     */
    public static Coordinate getFlagStart(String map,
                                          ETeamColor team) {
        float[] dimensions = getDimensions(map);
        float xAxisMid = dimensions[0] / 2.0f;
        float yAxisMid = dimensions[1] / 2.0f;
        
        float yOffset = yAxisMid / FLAGMULTIPLIER * (FLAGMULTIPLIER - 1);
        Coordinate coordinate = new Coordinate(xAxisMid, yAxisMid);
        switch (team) {
            case Red:
                coordinate = new Coordinate(xAxisMid, yAxisMid + yOffset);
                break;
            case Blue:
                coordinate = new Coordinate(xAxisMid, yAxisMid - yOffset);
                break;
            default:
                //shouldn't happen 
        }
        
        return coordinate;
    }
    
    /**
     * Get the goal position for the given team's flag on the given map.
     * 
     * @param map the map name
     * @param team the team to check the flag position
     * @return the goal position of the flag
     */
    public static Coordinate getFlagGoal(String map,
                                         ETeamColor team) {
        switch(team) {
            case Red:
                return getFlagStart(map, ETeamColor.Blue);
            case Blue:
                return getFlagStart(map, ETeamColor.Red);
            default:
                //shouldn't happen
        }
        return null;
    }
}
