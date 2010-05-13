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

package com.sun.darkstar.example.snowman.clientsimulator;

import com.sun.darkstar.example.snowman.clientsimulator.SimulatedPlayer.PLAYERSTATE;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingWorker;

/**
 * Program which will start and stop multiple simulated snowman game players.
 * It supports the following properties: <p>
 *
 * <dl style="margin-left: 1em">
 *
 * <dt> <i>Property:</i> <code><b>
 *	host
 *	</b></code><br>
 *	<i>Default:</i> {@code localhost}
 *
 * <dd style="padding-top: .5em">
 *      Specifies the host name of the snowman game server.<p>
 *
 * <dt> <i>Property:</i> <code><b>
 *	port
 *	</b></code><br>
 *	<i>Default:</i> {@code 3000}
 *
 * <dd style="padding-top: .5em"> 
 *	Specifies the port of the snowman game server.<p>
 *
 * <dt> <i>Property:</i> <code><b>
 *	maxClients
 *	</b></code><br>
 *	<i>Default:</i> {@code 100}
 *
 * <dd style="padding-top: .5em"> 
 *	Specifies maximun number of clients that can be started.<p>
 *
 *  <dt> <i>Property:</i> <code><b>
 *	moveDelay
 *	</b></code><br>
 *	<i>Default:</i> 2000 (two seconds)<br>
 *
 * <dd style="padding-top: .5em"> 
 * Specifies the minimun delay, in milliseconds, between when a player
 * will send a move message.<p>
 * 
 * <dt> <i>Property:</i> <code><b>
 *	newClientDelay
 *	</b></code><br>
 *	<i>Default:</i> 175<br>
 *
 * <dd style="padding-top: .5em"> 
 * Specifies the delay, in milliseconds, between new client
 * creation. This delay also applies to client destruction.<p>
 * 
 * <dt> <i>Property:</i> <code><b>
 *	minWaiting
 *	</b></code><br>
 *	<i>Default:</i> 0<br>
 *
 * <dd style="padding-top: .5em">
 * Specifies the number of players needed to be waiting. If no player is
 * waiting a new player is added until maxClients is reached. Players are only
 * added if no player is waiting.
 * 
 * <dt> <i>Property:</i> <code><b>
 *	numPlayersPerGame
 *	</b></code><br>
 *	<i>Default:</i> 2<br>
 *
 * <dd style="padding-top: .5em"> 
 * Specifies the number of players required to start a game. This is only used
 * in calculating the Total Number of Players field displayed on the control planel.
 * 
 * <dt> <i>Property:</i> <code><b>
 *	numRobotsPerGame
 *	</b></code><br>
 *	<i>Default:</i> 2<br>
 *
 * <dd style="padding-top: .5em"> 
 * Specifies the number of robots created for each game. This is only used
 * in calculating the Total Number of Robots field displayed on the control planel.
 * </dl> <p>
 * 
 * @author Jeffrey Kesselman
 * @author Keith Thompson
 */
public class ClientSimulator extends JFrame implements ChangeListener {
    static final long serialVersionUID = 1L;

    static private final Logger logger = Logger.getLogger(
            ClientSimulator.class.getName());
    
    private String hostname;
    
    private final String serverHost;
    private final String serverPort;
    private final String connectTimeout;
    private final int moveDelay;
    private final int newClientDelay;
    private final int minWaiting;
    private final int numPlayersPerGame;
    private final int numRobotsPerGame;
    private final int buildNumber;
    
    private final JSlider usersSlider;
    private final JLabel userCount;
    private final JProgressBar usersBar;
    private final JLabel userRealCount;
    private final JLabel gameCount;
    private final JLabel botCount;
    
    private final Queue<SimulatedPlayer> players =
            new ConcurrentLinkedQueue<SimulatedPlayer>();
    
    private final ChangeThread changeThread;
    
    /**
     * Create and display the client simulator slider.
     */
    ClientSimulator() throws Exception {
        super("Player Simulator Controls");
        
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch(Exception e) {
            hostname = "localhost";
        }
        
        serverHost = System.getProperty("host", "localhost");
        serverPort = System.getProperty("port", "3000");
        int timeout = Integer.getInteger("connectTimeout", 5000);
        if (timeout < 0) throw new Exception("connectTimeout can not be negative");
        connectTimeout = Integer.toString(timeout);
        logger.log(Level.CONFIG, "Players to use server at {0}:{1} with timeout of {2}",
                   new Object[] {serverHost, serverPort, connectTimeout});
                
        moveDelay = Integer.getInteger("moveDelay", 2000);
        logger.log(Level.CONFIG, "Move delay set to {0} milliseconds", moveDelay);
        
        newClientDelay = Integer.getInteger("newClientDelay", 175);
        logger.log(Level.CONFIG,
                   "New player delay set to {0} milliseconds", newClientDelay);

        final int maxClients = Integer.getInteger("maxClients", 100);
        logger.log(Level.CONFIG, "Max number of players set to {0}", maxClients);
        
        minWaiting = Integer.getInteger("minWaiting", 0);
        if (minWaiting != 0)
            logger.log(Level.CONFIG,
                       "Min number of waiting players set to {0}", minWaiting);

        numPlayersPerGame = Integer.getInteger("numPlayersPerGame", 2);
        numRobotsPerGame = Integer.getInteger("numRobotsPerGame", 2);
        
        buildNumber = Integer.getInteger("buildNumber", 0);
        logger.log(Level.CONFIG,
                   "Build Number set to {0}", buildNumber);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout(5,4));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(4,1,5,4));
        textPanel.add(new JLabel("Target Number of Players:"));
        textPanel.add(new JLabel("Actual Number of Players:"));
        textPanel.add(new JLabel("Total Number of Games:"));
        textPanel.add(new JLabel("Total Number of Bots:"));
      
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new GridLayout(4,1,5,4));
        usersSlider = new JSlider(0, maxClients);
        usersSlider.setValue(0);
        usersSlider.addChangeListener(this);
        usersBar = new JProgressBar(0, maxClients);
        usersBar.setValue(0);
        progressPanel.add(usersSlider);
        progressPanel.add(usersBar);
        gameCount = new JLabel("0");
        progressPanel.add(gameCount);
        botCount = new JLabel("0");
        progressPanel.add(botCount);
  
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(4,1,5,4));
        userCount = new JLabel("0");
        userRealCount = new JLabel("0");
        labelPanel.add(userCount);
        labelPanel.add(userRealCount);

        JTextArea profilePanel = new JTextArea(5, 20);
        
        c.add(textPanel, BorderLayout.WEST);
        c.add(progressPanel, BorderLayout.CENTER);
        c.add(labelPanel, BorderLayout.EAST);
        c.add(new JScrollPane(profilePanel), BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        changeThread = new ChangeThread();
        changeThread.execute();
        new MoveThread().start();
        new ProfileThread(profilePanel).start();
        setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        int val = usersSlider.getValue();
        String numstr = Integer.toString(val);
        userCount.setText(numstr);
        ClientSimulator.this.pack();
        userCount.repaint();
        changeThread.wakeUp();
    }
    
    // Thread to handle change in players
    private class ChangeThread extends SwingWorker<Void, Integer> {

        private final Lock lock = new ReentrantLock();
        private final Condition change = lock.newCondition();
        
        @Override
        public Void doInBackground() {
            int userId = 0;

            while (true) {
                int numGames = players.size() / numPlayersPerGame;
                gameCount.setText(Integer.toString(numGames));
                botCount.setText(Integer.toString(numGames * numRobotsPerGame));
                publish(players.size());

                if (usersSlider.getValue() > players.size()) {
                    
                    if (minWaiting != 0) {
                        int waiting = 0;
                        for (SimulatedPlayer player : players) {
                            if (player.isWaiting())
                                waiting++;
                        }
                        if (waiting < minWaiting)
                            addPlayer(userId++);
                        else
                            block(1000L);
                    } else
                        addPlayer(userId++);
                } else if (usersSlider.getValue() < players.size()) {
                    SimulatedPlayer p = players.peek();
                    if (p != null) {
                        p.quit();
                        players.poll();
                        pause(); // avoid storm
                    }
                } else {
                    block(Long.MAX_VALUE);
                }
            }
        }
        
        private void addPlayer(int id) {
            Properties properties = new Properties();
            properties.setProperty("host", serverHost);
            properties.setProperty("port", serverPort);
            properties.setProperty("connectTimeout", connectTimeout);
            properties.setProperty("name", hostname + "_" + buildNumber + "_Sim" + id);
            try {
                SimulatedPlayer player =
                        new SimulatedPlayer(properties, moveDelay);
                players.add(player);
            } catch (Exception ex) {
                logger.log(Level.SEVERE,
                           "Exception creating simulated player",
                           ex);
            }
            pause(); // avoid storm
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            Integer size = chunks.get(chunks.size()-1);
            usersBar.setValue(size);
            userRealCount.setText(size.toString());
        }
        
        private void block(long timeout) {
            lock.lock();
            try {
                change.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignore) {
            } finally {
                lock.unlock();
            }
        }
        
        void wakeUp() {
            lock.lock();
            change.signal();
            lock.unlock();
        }
        
        private void pause() {
            try {
                Thread.sleep(newClientDelay);
            } catch (InterruptedException ignore) {}
        }
    }
       
    // Thread to invoke player activity
    private class MoveThread extends Thread {

        @Override
        public void run() {

            while (true) {

                Iterator<SimulatedPlayer> iter = players.iterator();
                while (iter.hasNext()) {
                    try {
                        if (iter.next().move() == PLAYERSTATE.Quit) {
                            iter.remove();
                            changeThread.wakeUp();
                        }
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE,
                                   "Exception from player", ex);
                        iter.remove();
                        changeThread.wakeUp();
                    }
                    try {
                        sleep(50L);
                    } catch (InterruptedException ignore) {}
                }
                // If there are no players, keep from spinning
                if (players.size() == 0)
                    try {
                        sleep(500L);
                    } catch (InterruptedException ignore) {}
            }
        }
    }

    // Thread to monitor server profile output
    private class ProfileThread extends Thread {
        final JTextArea profilePanel;
        final int profilePort;
        
        ProfileThread(JTextArea profilePanel) {
            this.profilePanel = profilePanel;
            profilePort = Integer.getInteger("profilePort", 43007);
        }
        
        @Override
        public void run() {
            InputStream is = null;
            while (true) {
                try {
                    if (is == null) {
                        Socket socket = new Socket(serverHost, profilePort);
                        is = socket.getInputStream();
                        profilePanel.setText("Monitoring game server on " +
                                             serverHost + ":" + profilePort);
                    }
                    byte[] buff = new byte[1024];
                    int bytes = is.read(buff);
                    while (bytes > 0) {
                        profilePanel.setText(new String(Arrays.copyOf(buff, bytes)));
                        bytes = is.read(buff);
                    }
                } catch (Exception ex) {
                    profilePanel.setText("Exception connecting to game server on " +
                                         serverHost + ":" + profilePort + "\n");
                    profilePanel.append(ex.getLocalizedMessage());
                }
                is = null;
                try {
                    sleep(15 * 1000);
                } catch (InterruptedException ignore) {}
            }
        }
    }

    /**
     * Main.
     * @param args no arguments supported. See class description for
     * the list of properties supported.
     */
    public static void main(String[] args) throws Exception {
        new ClientSimulator();
    }
}
