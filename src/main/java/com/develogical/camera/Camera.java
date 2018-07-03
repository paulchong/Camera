package com.develogical.camera;

public class Camera {
    private final Sensor cameraSensor;
    private final MemoryCard cameraMemoryCard;
    private boolean isOn = false;
    private boolean isWriting = false;

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        cameraSensor = sensor;
        cameraMemoryCard = memoryCard;
    }

    public void pressShutter() {
        if(isOn) {
            cameraMemoryCard.write(cameraSensor.readData(), new WriteCompleteListener() {
                @Override
                public void writeComplete() {
                    isWriting=false;
                    if (!isOn) {
                        cameraSensor.powerDown();
                    }
                }
            });
            isWriting=true;
//            cameraMemoryCard.write(cameraSensor.readData(), null);



        }
    }

    public void powerOn() {
        isOn=true;
        cameraSensor.powerUp();
    }

    public void powerOff() {
        isOn=false;
        if (!isWriting) {
            cameraSensor.powerDown();
        }
    }
}

