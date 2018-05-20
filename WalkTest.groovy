import org.apache.commons.io.IOUtils;
import  com.neuronrobotics.bowlerstudio.physics.*;
import com.neuronrobotics.bowlerstudio.threed.*;

def base;
//Check if the device already exists in the device Manager
if(args==null){
	base=DeviceManager.getSpecificDevice( "CarlTheWalkingRobot",{
			//If the device does not exist, prompt for the connection
			
			MobileBase m = MobileBaseLoader.fromGit(
				"https://github.com/madhephaestus/carl-the-hexapod.git",
				"CarlTheRobot.xml"
				)
			if(m==null)
				throw new RuntimeException("Arm failed to assemble itself")
			println "Connecting new device robot arm "+m
			return m
		})
}else
	base=args.get(0)


println "walk forward 10 increments of 10 mm totalling 100 mm translation"
TransformNR move = new TransformNR(5,0,0,new RotationNR())
double toSeconds=0.03//100 ms for each increment
for(int i=0;i<10;i++){
	base.DriveArc(move, toSeconds);
	ThreadUtil.wait((int)toSeconds*1000)
}
println "Waiting for legs to reset"
ThreadUtil.wait(6000)// wait for the legs to fully reset themselves.

println "turn 20 increments of 2 degrees totalling 40 degrees turn"
move = new TransformNR(0,0,0,new RotationNR( 0,5,0))
for(int i=0;i<20;i++){
	base.DriveArc(move, toSeconds);
	ThreadUtil.wait((int)toSeconds*1000)
}
println "Waiting for legs to reset"
ThreadUtil.wait(6000)// wait for the legs to fully reset themselves.

println "walk sideways 10 increments of 10 mm totalling 100 mm translation"
move = new TransformNR(0,5,0,new RotationNR())
for(int i=0;i<10;i++){
	base.DriveArc(move, toSeconds);
	ThreadUtil.wait((int)toSeconds*1000)
}

println "Waiting for legs to reset"
ThreadUtil.wait(6000)// wait for the legs to fully reset themselves.

return null;