#include <Servo.h>

/*For more information, please visit: 
  http://www.swduino.com/?tab=examples&doc=control
  Version: 2.0
*/

#include <swDuino.h>

Servo servo1;
Servo servo2;
swDuino objswDuino;

int led = 13;
int angle = 0;
int pos = 0;

void setup() {
  // put your setup code here, to run once:
  objswDuino.begin(9600);
  pinMode(led, OUTPUT);
  servo1.attach(8);
  servo2.attach(7);
  //servo1.write(110);
  //servo2.write(110);

  // start with door closed
  for (pos = 110; pos >= 0; pos -= 1)   //from 110 to 0 (close door)
  {
    servo2.write(pos);
    servo1.write(pos);
    delay(15);
  }
  
}

void loop() {
//    for (pos = 0; pos <= 110; pos += 1)   //from 0 to 110 / (open door)
//    {
//      servo2.write(pos);
//      delay(15);
//    }
//
//    delay(3000);
//    
//    for (pos = 110; pos >= 0; pos -= 1)   //from 110 to 0 (close door)
//    {
//      servo2.write(pos);
//      delay(15);
//    }
  
  // put your main code here, to run repeatedly:
  objswDuino.read(trigger);
  delay(500);
}

void trigger(String VARIABLE, String VALUE) {
  if (VARIABLE == "cmd1" && VALUE == "open")
  {
    for (pos = 0; pos <= 110; pos += 1)   //from 0 to 110 / (open door)
    {
      servo1.write(pos);
      delay(15);
    }

    delay(3000);
    
    for (pos = 110; pos >= 0; pos -= 1)   //from 110 to 0 (close door)
    {
      servo1.write(pos);
      delay(15);
    }
  }
  if (VARIABLE == "cmd2" && VALUE == "open")
  {
    for (pos = 0; pos <= 110; pos += 1)   //from 0 to 110 / (open door)
    {
      servo2.write(pos);
      delay(15);
    }

    delay(3000);
    
    for (pos = 110; pos >= 0; pos -= 1)   //from 110 to 0 (close door)
    {
      servo2.write(pos);
      delay(15);
    }
  }
}
