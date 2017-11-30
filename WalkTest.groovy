import org.apache.commons.io.IOUtils;
import  com.neuronrobotics.bowlerstudio.physics.*;
import com.neuronrobotics.bowlerstudio.threed.*;

MobileBase base;
Object dev = DeviceManager.getSpecificDevice(MobileBase.class, "jaguar");
println "found: "+dev
//Check if the device already exists in the device Manager
if(args==null){
	if(dev==null){
		//Create the kinematics model from the xml file describing the D-H compliant parameters. 
		def file=["https://github.com/madhephaestus/carl-the-hexapod.git","CarlTheRobot.xml"]as String[]
		String xmlContent = ScriptingEngine.codeFromGit(file[0],file[1])[0]
		MobileBase mb =new MobileBase(IOUtils.toInputStream(xmlContent, "UTF-8"))
		mb.setGitSelfSource(file)
		mb.connect()
		DeviceManager.addConnection(mb,mb.getScriptingName())
		base=mb
		println "Waiting for cad to generate"
		ThreadUtil.wait(1000)
		while(MobileBaseCadManager.get( base).getProcesIndictor().getProgress()<1){
			println "Waiting for cad to get to 1:"+MobileBaseCadManager.get( base).getProcesIndictor().getProgress()
			ThreadUtil.wait(1000)
		}
	}else{
		println "Robot found, runing code"
		//the device is already present on the system, load the one that exists.
	  	base=(MobileBase)dev
	}
}else
	base=(MobileBase)args.get(0)

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