#!/bin/bash

activeDir=/home/ec2-user/IceBreaker/Server
gitDir=/home/ec2-user/IceBreaker
package=ice_breaker_server/adam4/com/
config=`cat /home/ec2-user/iceConfig.txt`
status=`mktemp`



cd $gitDir && git reset --hard HEAD >> $status
cd $gitDir && git pull >> $status


if [ "$status" != "Already up-to-date." ];
then
cd $gitDir && git fetch origin >> $status
cd $gitDir && git reset --hard origin/master >> $status
fi

# kill active 
rm $activeDir/*.run 2> /dev/null

#  /home/ec2-user/IceBreaker/Server/src/ice_breaker_server/adam4/com/
cd $gitDir/Server/src/$package/ && javac *.java >> $status

mkdir /home/ec2-user/IceBreaker/Server/bin/ice_breaker_server
mkdir /home/ec2-user/IceBreaker/Server/bin/ice_breaker_server/adam4
mkdir /home/ec2-user/IceBreaker/Server/bin/ice_breaker_server/adam4/com/

mv $gitDir/Server/src/$package/*.class $activeDir/bin/$package

cd /home/ec2-user/IceBreaker/Server/bin && java ice_breaker_server/adam4/com/IceBreakerServer

#allow chance for it to gracefully stop
sleep $graceTime
#force stop if it fails to stop gracefully
pkill -9 java
#restart

# ctrl z + bg
nohup java -cp $activeDir/lib/*:$activeDir/bin/ $package $config >> /dev/null 2>> /dev/null &

#give time for it to start-up
sleep $graceTime

fi


#run test client
nohup java -cp $activeDir/lib/*:$activeDir/bin/ $package.TestClient $url >> $status 2>>  $status &
#give test client time to test
sleep $graceTime

echo `date` " $1 $2 " >> $status

if [ $1 ]
then
cat $status | tr -cd '\11\12\15\40-\176' | mail -s "SFA code update status" $1
else
cat $status
fi

rm $status






