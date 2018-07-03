package com.develogical.camera;

import org.junit.*;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class CameraTest {
    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        Sensor sensor = mock(Sensor.class);
        MemoryCard memoryCard = mock(MemoryCard.class);

        Camera cam = new Camera(sensor, memoryCard);

        cam.powerOn();
        verify(sensor).powerUp();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        Sensor sensor = mock(Sensor.class);
        MemoryCard memoryCard = mock(MemoryCard.class);
        Camera cam = new Camera(sensor, memoryCard);

        cam.powerOff();
        verify(sensor).powerDown();  }


    @Test
    public void pressingShutterWithPowerOn() {
        Sensor sensor = mock(Sensor.class);
        MemoryCard memoryCard = mock(MemoryCard.class);
        Camera cam = new Camera(sensor, memoryCard);


        cam.powerOn();
        cam.pressShutter();
        verify(memoryCard).write(any(),any() );
    }

    @Test
    public void pressingShutterWithPowerOff() {
        MemoryCard memoryCard = mock(MemoryCard.class);
        Sensor sensor = mock(Sensor.class);

        Camera cam = new Camera(sensor, memoryCard);

        cam.powerOff();
        cam.pressShutter();

        verifyNoMoreInteractions(memoryCard);
    }

    @Test
    public void doesNotPowerDownSensorWhenStillWritingData() {
        MemoryCard memoryCard = mock(MemoryCard.class);
        Sensor sensor = mock(Sensor.class);

        Camera cam = new Camera(sensor, memoryCard);

        cam.powerOn();
        cam.pressShutter();
        cam.powerOff();

        verify(sensor, never()).powerDown();
    }


    @Test
    public void powersDownWhenWritingComplete() {
        MemoryCard memoryCard = mock(MemoryCard.class);
        Sensor sensor = mock(Sensor.class);

        Camera cam = new Camera(sensor, memoryCard);

        cam.powerOn();
        cam.pressShutter();
        cam.powerOff();

        ArgumentCaptor<WriteCompleteListener> argument = ArgumentCaptor.forClass(WriteCompleteListener.class);

        verify(memoryCard).write(any(), argument.capture());

        verify(sensor, never()).powerDown();

        argument.getValue().writeComplete();

        verify(sensor).powerDown();
    }

    @Test
    public void doesNotPowersDownWhenWritingCompleteIfCameraIsOn() {
        MemoryCard memoryCard = mock(MemoryCard.class);
        Sensor sensor = mock(Sensor.class);

        Camera cam = new Camera(sensor, memoryCard);

        cam.powerOn();
        cam.pressShutter();

        ArgumentCaptor<WriteCompleteListener> argument = ArgumentCaptor.forClass(WriteCompleteListener.class);

        verify(memoryCard).write(any(), argument.capture());

        argument.getValue().writeComplete();

        verify(sensor, never()).powerDown();
    }

}
