#include <SoftwareSerial.h>// import the serial library
int ledpin=13;
int i=0; // led on D13 will show blink on / off
int BluetoothData; // the data given from Computer


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  while (!Serial){};
  Serial.println("Bluetooth On please press 1 or 0 blink LED ..");
    pinMode(ledpin,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
 
   if (Serial.available()>0){
     BluetoothData=Serial.read();
     Serial.println(BluetoothData);    
     if (i == 0) i = 1;
     else i= 0;
     digitalWrite(ledpin,i);
  }
}
