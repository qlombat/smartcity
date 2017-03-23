import com.phidgets.*;
import com.phidgets.event.*;

public class RfidServo{
    public static final void main(String args[]) throws Exception {
        final RFIDPhidget rfid;
        final AdvancedServoPhidget servo;
        final InterfaceKitPhidget ik;

        servo = new AdvancedServoPhidget();
        rfid = new RFIDPhidget();
        ik = new InterfaceKitPhidget();

        ik.addAttachListener(new AttachListener() {
            public void attached(AttachEvent ae) {
                System.out.println("attachment of InterfaceKitPhidget " + ae);
            }
        });
        ik.addDetachListener(new DetachListener() {
            public void detached(DetachEvent ae) {
                System.out.println("detachment of InterfaceKitPhidget " + ae);
            }
        });

        servo.addAttachListener(new AttachListener() {
            public void attached(AttachEvent ae) {
                System.out.println("attachment of AdvancedServoPhidget " + ae);
            }
        });
        servo.addDetachListener(new DetachListener() {
            public void detached(DetachEvent ae) {
                System.out.println("detachment of AdvancedServoPhidget " + ae);
            }
        });

        rfid.addAttachListener(new AttachListener() {
            public void attached(AttachEvent ae)
            {
                try
                {
                    ((RFIDPhidget)ae.getSource()).setAntennaOn(true);
                    ((RFIDPhidget)ae.getSource()).setLEDOn(true);
                }
                catch (PhidgetException ex) { }
                System.out.println("attachment of RFIDPhidget " + ae);
            }
        });
        rfid.addDetachListener(new DetachListener() {
            public void detached(DetachEvent ae) {
                System.out.println("detachment of RFIDPhidget " + ae);
            }
        });

        rfid.addTagGainListener(new TagGainListener() {
            public void tagGained(TagGainEvent oe)
            {
                System.out.println("Tag Gained: " +oe.getValue());
                try{openBarrier(servo, oe.getValue(),ik);}
                catch(Exception e){
                    System.out.println(e);
                }
            }
        });
        rfid.addTagLossListener(new TagLossListener() {
            public void tagLost(TagLossEvent oe)
            {
                System.out.println(oe);
            }
        });

        rfid.openAny();
        servo.openAny();
        ik.openAny();

        servo.waitForAttachment();
        rfid.waitForAttachment();
        ik.waitForAttachment();

        servo.setEngaged(0, false);
        servo.setSpeedRampingOn(0, false);
        servo.setPosition(0, 100);
        servo.setEngaged(0, true);

        System.in.read();
        System.out.print("closing...");
        rfid.close();
        servo.close();
        ik.close();
    }

    public static void openBarrier(AdvancedServoPhidget servo, String rfidTag, InterfaceKitPhidget ik) throws Exception {
        if (rfidTag.equals("2800b86b50")) {
            Thread threadLight = new Thread(new Runnable(){
            public void run(){
                try{
                    for (int i = 0; i <12; i++){
                        ik.setOutputState(0,true);
                        Thread.sleep(300);
                        ik.setOutputState(0,false);
                        Thread.sleep(300);
                    }
                }
                catch(Exception e){};
            }
            });

            threadLight.start();
            servo.setSpeedRampingOn(0, true);
            servo.setAcceleration(0,100);
            servo.setVelocityLimit(0, 200);
            servo.setPosition(0, 190);
            Thread.sleep(5000);
            servo.setPosition(0, 100);
        } else {
            return;
        }
    }
}