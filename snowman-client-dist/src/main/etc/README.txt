Project Snowman Client Distribution
===================================

The Project Snowman Client Distribution contains three bundled components:
* Project Snowman Graphical Client
* Project Snowman Client Simulator
* Project Snowman World Editor

The Graphical Client and Client Simulator are both designed to be run against a
running Project Snowman Server.  The World Editor is a standalone application
for creating and modifying game worlds.



RUNNING INSTRUCTIONS:
=====================

After unpacking the distribution, there are three executable JAR files located
in the same directory as this README file.  Each respectively can be executed
to launch the Graphical Client, Client Simulator, and World Editor.  For example:

Project Snowman Graphical Client

java -jar snowman-client.jar

Project Snowman Client Simulator

java -jar snowman-client-simulator.jar

Project Snowman World Editor

java -jar snowman-world-editor.jar



AVAILABLE PROPERTIES:
=====================

Runtime of each of the above JAR files can be configured via various available
system properties.  For example, by default, the Graphical Client and Client
Simulator attempt to connect to a running Project Snowman Server on localhost
at port 3000.  This can be overriden like so:

java -jar snowman-client.jar -Dserver.host=host.domain -Dserver.port=12345


property name:  server.host
default value:  localhost
description:    Host name of application server node. Used in the client
		and client simulator and should be set to the name of the host
		of a running Project Snowman server.

property name:  server.port
default value:  3000
description:    Host port of application server. Used in the client and
		client simulator.

property name:	maxClients
default value:	100
description:	Maximum number of clients that a client simulator VM can startup.

property name:	newClientDelay
default value:	175
description:	Delay in milliseconds between adding clients to the server
		with the client simulator.

property name:	move.delay
default value:	5000
description:	Minimum time required in ms between simulated client moves
		in the client simulator.



EXAMPLES:
=========

To connect the client to localhost

   java -jar snowman-client.jar

To connect the client simulator to localhost with a maximum of 500 simulated clients

   java -jar snowman-client-simulator.jar -DmaxClients=500

To run the world editor

   java -jar snowman-world-editor.jar
