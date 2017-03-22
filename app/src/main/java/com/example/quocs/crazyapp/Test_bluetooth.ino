//motor A connected between A01 and A02
//motor B connected between B01 and B02
#define MAX_SPEED 255
//define directory
#define GO_STOP 0
#define GO_UP 1
#define GO_DOWN 2
#define GO_LEFT 3
#define GO_RIGHT 4
#define GO_STOP_TURN 5

int STBY = 10; //standby

//Motor A
int PWMA = 3; //Speed control 
int AIN1 = 8; //Direction
int AIN2 = 9; //Direction

//Motor B
int PWMB = 5; //Speed control
int BIN1 = 11; //Direction
int BIN2 = 12; //Direction

//SPEED
int RUN_SPEED = 255;
//Bluetooth
int BluetoothData;
boolean isGoLR;
int isGoUp; 
void setup(){
  pinMode(STBY, OUTPUT);
  digitalWrite(STBY, HIGH);
  pinMode(PWMA, OUTPUT);
  pinMode(AIN1, OUTPUT);
  pinMode(AIN2, OUTPUT);

  pinMode(PWMB, OUTPUT);
  pinMode(BIN1, OUTPUT);
  pinMode(BIN2, OUTPUT);
  Serial.begin(9600);
  while (!Serial){};
  Serial.println("Init Serial sucess at baud 9600");
  //speed = MAX_SPEED;
  STOP();
  isGoUp = 2;
  isGoLR = false;
  
}
void reverse(int wheel1,int wheel2 ){
  digitalWrite(wheel1, HIGH);
  digitalWrite(wheel2, LOW);
}
void forward(int wheel1,int wheel2){
  digitalWrite(wheel1, LOW);
  digitalWrite(wheel2, HIGH);
}
void UP(int speed){
  analogWrite(PWMA, speed);
  analogWrite(PWMB, speed);
  forward(AIN1,AIN2);
  forward(BIN1,BIN2);
  isGoUp = 1;
}
void DOWN(int speed){
  analogWrite(PWMA, speed);
  analogWrite(PWMB, speed);
  reverse(AIN1,AIN2);
  reverse(BIN1,BIN2);
  isGoUp = 0;
}
void RIGHT(int speed){
  analogWrite(PWMA, speed);
  analogWrite(PWMB, speed);
  reverse(AIN1,AIN2);
  forward(BIN1,BIN2);
}
void LEFT(int speed){
  analogWrite(PWMA, speed);
  analogWrite(PWMB, speed);
  forward(AIN1,AIN2);
  reverse(BIN1,BIN2);
}
void STOP(){
  analogWrite(PWMA, 0); 
  analogWrite(PWMB, 0);
  digitalWrite(AIN1, LOW);
  digitalWrite(AIN2, LOW);
  digitalWrite(BIN1, LOW);
  digitalWrite(BIN2, LOW);
}

void resetWheel(){
   analogWrite(PWMA, MAX_SPEED);
   analogWrite(PWMB, MAX_SPEED);
}
void smoothLeft(){
  analogWrite(PWMB, MAX_SPEED/2);
}

void smoothRight(){
  analogWrite(PWMB, MAX_SPEED/2);
}

boolean isRun = false;
void loop(){
   if (Serial.available()>0){
    BluetoothData=Serial.read();
    Serial.print("Data receive ");
    Serial.print(BluetoothData);
    Serial.print(":    ");
    switch (BluetoothData){
      case GO_UP:  
        Serial.println("GO UP ");
        UP(MAX_SPEED);
        isRun = true;
        break;
      case GO_DOWN:
        Serial.println("GO DOWN ");
        DOWN(MAX_SPEED);
        isRun = true;
        break;
      case GO_LEFT:
        if (isRun) {
          smoothLeft();
           Serial.println("GO SMOOTH LEFT ");
        }
        else {
          LEFT(MAX_SPEED/2);
           Serial.println("GO LEFT ");
        }
        //delay(100);
        break;
      case GO_RIGHT:
        if (isRun) {
          smoothRight();
          Serial.println("GO SMOOTH RIGHT ");
        }
        else {
          Serial.println("GO RIGHT ");
          RIGHT(MAX_SPEED/2);
        }
        break;
      case GO_STOP:
        Serial.println("GO STOP ");
        isRun = false;
        STOP();
        break;
      case GO_STOP_TURN:
        resetWheel();
        Serial.println("GO STOP TURN ");
        break;
      default:
        Serial.println("Can not understand command ");
    }    
   }
}


