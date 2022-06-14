/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * Copyright (c) 2022 STMicroelectronics.
  * All rights reserved.
  *
  * This software is licensed under terms that can be found in the LICENSE file
  * in the root directory of this software component.
  * If no LICENSE file comes with this software, it is provided AS-IS.
  *
  ******************************************************************************
  */
/* USER CODE END Header */
/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "cmsis_os.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

#include "../../PeripheralDriver/Inc/oled.h"
#include "../../PIDController/pid.h"
#include "../../ICM20948/Inc/ICM20948.h"

/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
ADC_HandleTypeDef hadc1;
DMA_HandleTypeDef hdma_adc1;

I2C_HandleTypeDef hi2c1;

TIM_HandleTypeDef htim1;
TIM_HandleTypeDef htim2;
TIM_HandleTypeDef htim3;
TIM_HandleTypeDef htim8;

UART_HandleTypeDef huart3;

osThreadId defaultTaskHandle;
osThreadId MotorTaskHandle;
osThreadId EncoderTaskHandle;
osThreadId Encoder_BTaskHandle;
osThreadId ServoTaskHandle;
osThreadId ShowTaskHandle;
osThreadId fakePIDHandle;
osThreadId UltraSonic_TaskHandle;
osThreadId DMA_ADC_TaskHandle;
osThreadId ICM20948Handle;
/* USER CODE BEGIN PV */

PID_TypeDef TPID;
PID_TypeDef TPID2;


uint8_t aRxBuffer[4];
uint8_t DONE[7] = "STMDONE";
//SHABADAMA

double pwmVal_set;

double diff1 ;
double diff2 ;

const double circumference = 20.74;

double rpm_A, rpm_B;
double speed_A, speed_B;

double distance = 0;
double distance_b = 0;

int choice = 0;

char instruction;
int number;

int isTurning = 0;
//int stop_motor = 0;

int IC_Val1 = 0;
int IC_Val2 = 0;
int difference_US = 0;
int first_capture = 0;

int distance_US = 0;

uint16_t dir2, dir1;
int distance_IR = 0;

int distance_from_center_challenge2 = 0;
int distance_to_center_challenge2 = 0;
int distance_back_to_center_challenge2 = 0;

uint16_t readings[3];
int ICM_angle = 0;
int ICM_angle_2 = 0;

//////////////////////////////MOVE SETTINGS////////////////////////////////////////
float center = 148;

double pwmVal1;
double pwmVal2;

//int left_turn_value = 105;		//Amount in CM for a full 360 degree turn turning left
//int right_turn_value = 116;		//Amount in CM for a full 360 degree turn turning right
//int leftback_turn_value = 105;
//int rightback_turn_value = 116;

int left_ICM_value_90 = 4750;
int right_ICM_value_90 = 4700;
int leftback_ICM_value_90 = 5200;
int rightback_ICM_value_90 = 5200;

int left_ICM_value_90_P = 5400;
int right_ICM_value_90_O = 5400;

float nonturn_multiplier = 1.23;  		// CAN BE USED

int obstacle_number = 0;
int total_obstacle = 6;
int c_lessthan10 = 0;


int a = 40;
int c = 30;	//If C < 30, make it 30
int b = 80; //Plus 10 to be safe -> (a+c+10)
int more_than_150 = 1;
int initial_distance_traveled = 40; //55 minimum distyance for sensing

/////////////////////////////////////////////////////////////////////////////////////
/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_TIM3_Init(void);
static void MX_TIM8_Init(void);
static void MX_TIM1_Init(void);
static void MX_TIM2_Init(void);
static void MX_ADC1_Init(void);
static void MX_DMA_Init(void);
static void MX_USART3_UART_Init(void);
static void MX_I2C1_Init(void);
void StartDefaultTask(void const * argument);
void Motor_Task(void const * argument);
void Encoder_Task(void const * argument);
void Servo_Task(void const * argument);
void Show_Task(void const * argument);
void fake_PID(void const * argument);
void US_Task(void const * argument);
void DMA_ADC_task(void const * argument);
void ICM20948_Task(void const * argument);

/* USER CODE BEGIN PFP */

/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

void move_forward(int distance_desired)
{
	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;


  /* Infinite loop */
	if (distance_desired <30)
	{
		pwmVal1 = 1000;
		pwmVal2 = 1000;
	}
	else
	{
		pwmVal1 = 3000;
		pwmVal2 = 3000;
	}


	HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{


	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  if ((distance+distance_b)/2 >= distance_desired * nonturn_multiplier)
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  distance = 0;
		  distance_b = 0;
		 osDelay(100);

		 return;
		}

	  osDelay(time_interval);
	}

}

void move_forward_challenge2(int distance_desired)
{
	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;


  /* Infinite loop */
	if (distance_desired <30)
	{
		pwmVal1 = 4500;
		pwmVal2 = 4500;
	}
	else
	{
		pwmVal1 = 4500;
		pwmVal2 = 4500;
	}


	HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{


	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  if ((distance+distance_b)/2 >= distance_desired * nonturn_multiplier)
	  	  {
		  //__HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  //__HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  distance = 0;
		  distance_b = 0;
		 //osDelay(100);

		 return;
		}

	  osDelay(time_interval);
	}

}


void move_forward_soft(int distance_desired)
{
	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;


  /* Infinite loop */
	  pwmVal1 = 500;
	  pwmVal2 = 500;

	HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{


	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  if ((distance+distance_b)/2 >= distance_desired * nonturn_multiplier)
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  distance = 0;
		  distance_b = 0;
		 osDelay(100);

		 return;
		}

	  osDelay(time_interval);
	}

}

void move_backward(int distance_desired)
{
	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;


	uint8_t hello[20];

  /* Infinite loop */
	if (distance_desired <30)
	{
		pwmVal1 = 1000;
		pwmVal2 = 1000;
	}
	else
	{
		pwmVal1 = 2000;
		pwmVal2 = 2000;
	}

	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);


//	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
//	  	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
//	  	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
//	  	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{

	  osDelay(time_interval);

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  htim1.Instance->CCR4 =center + 2; //left

	  if ((distance+distance_b)/2 > distance_desired * nonturn_multiplier)
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 htim1.Instance->CCR4 =center;
		 return;
		}
	}

}

void move_backward_soft(int distance_desired)
{
	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;


	uint8_t hello[20];

  /* Infinite loop */
	  pwmVal1 = 500;
	  pwmVal2 = 500;

	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);


//	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
//	  	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
//	  	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
//	  	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{

	  osDelay(time_interval);

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  htim1.Instance->CCR4 =center + 2; //left

	  if ((distance+distance_b)/2 > distance_desired * nonturn_multiplier)
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 htim1.Instance->CCR4 =center;
		 return;
		}
	}

}

void move_left(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 120 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;

	uint8_t hello[20];

	int counter = 0;
	move_forward(1);

  /* Infinite loop */
	  pwmVal1 = 2000;
	  pwmVal2 = 1000;

	  ICM_angle = 0;

	  //move_forward(4);

	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  htim1.Instance->CCR4 =95; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > left_turn_value * angle/360.0 )
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/

	  if (ICM_angle > left_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }


	}
	return;
}

void move_right(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 230 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;

	ICM_angle = 0;

	uint8_t hello[20];

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 1000;
	  pwmVal2 = 2000;



	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  htim1.Instance->CCR4 =236; //left

	  osDelay(time_interval);
	  //counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2 > right_turn_value * angle/360.0 )
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}
	  */
	  if (ICM_angle > right_ICM_value_90 * angle/90)
	  {

		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(1000);
		 move_backward(2);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }

	}
	return;
}

void move_left_back(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 120 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;

	uint8_t hello[20];

	ICM_angle = 0;

	int counter = 0;

	move_forward(1);

  /* Infinite loop */
	  pwmVal1 = 2000;
	  pwmVal2 = 1000;

	while(1)
	{
//		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
//	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
//	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);
//	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);

	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  htim1.Instance->CCR4 =95; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > leftback_turn_value * angle/360.0)
	  	  {
		  htim1.Instance->CCR4 =center;
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/

	  if (ICM_angle > leftback_ICM_value_90 * angle/90)
	  	  {
	  		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
	  		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
	  		  htim1.Instance->CCR4 =center;
	  		 osDelay(1000);
	  		 move_backward(2);
	  		 distance = 0;
	  		 distance_b = 0;
	  		 isTurning = 0;
	  		 ICM_angle = 0;
	  		 break;
	  	  }
	}
	return;
}

void move_right_back(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 230 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;


	uint8_t hello[20];

	ICM_angle = 0;

	int counter = 0;
	move_forward(6);

  /* Infinite loop */
	  pwmVal1 = 1000;
	  pwmVal2 = 2000;

	while(1)
	{
		osDelay(time_interval);
		HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  htim1.Instance->CCR4 =239; //left


	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > rightback_turn_value * angle/360.0)
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/



	  if (ICM_angle > rightback_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(800);
		 move_backward(2);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }
	}
	return;
}

void move_forward_till_turn()
{
	int time_interval = 10;
	int counter = 0;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;
	isTurning = 1;


  /* Infinite loop */
	  //pwmVal1 =1000;
	  //pwmVal2 =1000;

	HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{
		pwmVal1 =1500;
		pwmVal2 =1500;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  if (distance_IR <= 245 && distance_IR >= 60)
	  {

		  if (counter == 1)
		  {
			  distance_back_to_center_challenge2 = (distance + distance_b)/2;
			  move_right_normal(90); //center
			  move_forward(010);
			  move_right_normal(90);
			  counter++;
			  osDelay(time_interval);
		  }

		  if (counter == 0)
		  {
			  distance_from_center_challenge2 = (distance + distance_b)/2;
			  move_right_normal(180); //center
			  //move_forward(010);
			  //move_right_normal(90);
			  counter++;
			  osDelay(time_interval);
		  }
	  }

	  if (counter == 2)
	  {
		  move_forward(distance_back_to_center_challenge2 - distance_from_center_challenge2);
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  return;
	  }

	  osDelay(time_interval);
	}

}

void move_forward_till_UR(void)
{
	//Lets say, 9.75s for 360 degrees (Reject)
		// Lets say, 60cm for 180degrees, 230 cm for 360cm
		isTurning = 1;

		int time_interval = 10;

		//int counter = 0;

		//pwmVal1 = 5000;
		//pwmVal2 = 5000;
		HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
		HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

		uint8_t hello[20];


		int turned = 0;

	  /* Infinite loop */
		  pwmVal1 = 3000;
		  pwmVal2 = 3000;

		  if (more_than_150)
		  {
			  move_forward_challenge2(initial_distance_traveled);
		  }

		while(1)
		{
			HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
		  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
		  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
		  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

		  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);



		  osDelay(time_interval);

		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

		  if (distance_US < 25 && distance_US > 5)

		  	  {
			  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
			  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
			  distance = 0;
			  distance_b = 0;

			 return;
			}
}
}

void move_left_soft(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 120 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;

	uint8_t hello[20];

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 2000;
	  pwmVal2 = 1000;

	  ICM_angle = 0;

	  htim1.Instance->CCR4 =115; //left
	  osDelay(500);

	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  //htim1.Instance->CCR4 =115; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > left_turn_value * angle/360.0 )
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/

	  if (ICM_angle > left_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }


	}
	return;
}

void move_left_normal(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 120 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;

	uint8_t hello[20];

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 4500;
	  pwmVal2 = 2250;

	  ICM_angle = 0;

	  htim1.Instance->CCR4 = 95; //left
	  //osDelay(500);

	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  htim1.Instance->CCR4 = 95; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > left_turn_value * angle/360.0 )
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/

	  if (ICM_angle > left_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }


	}
	return;
}



void move_right_back_soft(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 230 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;


	uint8_t hello[20];

	ICM_angle = 0;

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 1000;
	  pwmVal2 = 2000;

	  htim1.Instance->CCR4 =210;
	  osDelay(500);

	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  //htim1.Instance->CCR4 =210; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > rightback_turn_value * angle/360.0)
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/



	  if (ICM_angle > rightback_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }
	}
	return;
}

void move_right_back_normal(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 230 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;


	uint8_t hello[20];

	ICM_angle = 0;

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 1000;
	  pwmVal2 = 2000;

	  htim1.Instance->CCR4 =239; //left
	  osDelay(500);

	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  //htim1.Instance->CCR4 =239; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > rightback_turn_value * angle/360.0)
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/



	  if (ICM_angle > rightback_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }
	}
	return;
}



void move_right_soft(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 230 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;

	ICM_angle = 0;

	uint8_t hello[20];

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 1000;
	  pwmVal2 = 2000;

	  htim1.Instance->CCR4 =210; //left

	  osDelay(500);

	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  //htim1.Instance->CCR4 =210; //left

	  osDelay(time_interval);
	  //counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2 > right_turn_value * angle/360.0 )
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}
	  */
	  if (ICM_angle > right_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }

	}
	return;
}

void move_right_normal(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 230 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;

	ICM_angle = 0;

	uint8_t hello[20];

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 2250;
	  pwmVal2 = 4500;

	  htim1.Instance->CCR4 =239;
	  //osDelay(500);

	while(1)
	{
		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  //htim1.Instance->CCR4 =239; //left

	  osDelay(time_interval);
	  //counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2 > right_turn_value * angle/360.0 )
	  	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}
	  */
	  if (ICM_angle > right_ICM_value_90 * angle/90)
	  {
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  htim1.Instance->CCR4 =center;
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 ICM_angle = 0;
		 break;
	  }

	}
	return;
}

void move_left_back_soft(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 120 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;

	uint8_t hello[20];

	ICM_angle = 0;

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 2000;
	  pwmVal2 = 1000;

	  htim1.Instance->CCR4 =115; //left
	  osDelay(500);

	while(1)
	{
//		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
//	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
//	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);
//	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);

	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  //htim1.Instance->CCR4 =115; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > leftback_turn_value * angle/360.0)
	  	  {
		  htim1.Instance->CCR4 =center;
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/

	  if (ICM_angle > leftback_ICM_value_90 * angle/90)
	  	  {
	  		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
	  		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
	  		  htim1.Instance->CCR4 =center;
	  		 osDelay(100);
	  		 distance = 0;
	  		 distance_b = 0;
	  		 isTurning = 0;
	  		 ICM_angle = 0;
	  		 break;
	  	  }
	}
	return;
}

void move_left_back_normal(double angle)
{

	//Lets say, 9.75s for 360 degrees (Reject)
	// Lets say, 60cm for 180degrees, 120 cm for 360cm
	isTurning = 1;

	int time_interval = 10;

	//int counter = 0;

	//pwmVal1 = 5000;
	//pwmVal2 = 5000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);
	distance_b = 0;
	distance = 0;

	uint8_t hello[20];

	ICM_angle = 0;

	int counter = 0;

  /* Infinite loop */
	  pwmVal1 = 2000;
	  pwmVal2 = 1000;

	  htim1.Instance->CCR4 =95;
	  osDelay(500);

	while(1)
	{
//		HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
//	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
//	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);
//	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);

	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);

	  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	  //htim1.Instance->CCR4 =95; //left

	  osDelay(time_interval);
	  counter++;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  /*if ((distance_b+distance)/2.0 > leftback_turn_value * angle/360.0)
	  	  {
		  htim1.Instance->CCR4 =center;
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		 osDelay(100);
		 distance = 0;
		 distance_b = 0;
		 isTurning = 0;
		 break;
		}*/

	  if (ICM_angle > leftback_ICM_value_90 * angle/90)
	  	  {
	  		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
	  		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
	  		  htim1.Instance->CCR4 =center;
	  		 osDelay(100);
	  		 distance = 0;
	  		 distance_b = 0;
	  		 isTurning = 0;
	  		 ICM_angle = 0;
	  		 break;
	  	  }
	}
	return;
}

void move_till_left(int move_some)
{
	//Lets say, 9.75s for 360 degrees (Reject)
		// Lets say, 60cm for 180degrees, 230 cm for 360cm
		isTurning = 1;

		int time_interval = 10;

		//int counter = 0;

		//pwmVal1 = 5000;
		//pwmVal2 = 5000;
		HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
		HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

		uint8_t hello[20];


		int turned = 0;

	  /* Infinite loop */
		  if (move_some)
		  {
			  move_forward_challenge2(initial_distance_traveled);
		  }
		  pwmVal1 = 3500;
		  pwmVal2 = 3500;

		while(1)
		{
			HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
		  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
		  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
		  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

		  HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);



		  osDelay(time_interval);

		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

		  if (distance_US < 30)

		  	  {
			  //distance_to_center_challenge2 = (distance + distance_b)/2;
			  distance = 0;
			  distance_b = 0;
			  move_left_normal(85.0);
			  turned = 1;


			 return;
			}


}
}

void move_forced_c()
{
	int time_interval = 10;
	int counter = 0;

	int c_done = 0;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;
	//isTurning = 1;


  /* Infinite loop */
	  //pwmVal1 =1000;
	  //pwmVal2 =1000;

	HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{
		pwmVal1 =3000;
		pwmVal2 =3000;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  if (c_done == 1)
	  {
		  return;
		  //go B part
	  }

	  if ((distance + distance_b)/2 > (c-10))
	  {
		  c_done = 1;
	  }

	  osDelay(time_interval);
	}

}

void move_forced_b()
{
	int time_interval = 10;
	int counter = 0;

	int b_done = 0;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;
	//isTurning = 1;


  /* Infinite loop */
	  //pwmVal1 =1000;
	  //pwmVal2 =1000;

	HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{
		pwmVal1 =4000;
		pwmVal2 =4000;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  if (b_done == 1)
	  {
		  if (c <= 10)
		  {
			  move_right_normal(90);
			  c_lessthan10 = 1;
		  }
		  else
		  {
			  move_right_normal(180);
		  }
		  //move_forward(05);
		  //move_right_normal(90);
		  move_forced_c();
		  return;
		  //go B part
	  }

	  if ((distance + distance_b)/2 > b)
	  {
		  b_done = 1;
	  }

	  osDelay(time_interval);
	}

}

void move_forced()
{
	int time_interval = 10;
	int counter = 0;

	int a_done = 0;
	int b_done = 0;

	//int counter = 0;

	//pwmVal1 = 3000;
	//pwmVal2 = 3000;
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
	HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

	distance_b = 0;
	distance = 0;
	//isTurning = 1;


  /* Infinite loop */
	  //pwmVal1 =1000;
	  //pwmVal2 =1000;

	HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
	  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
	  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

	while(1)
	{
		pwmVal1 =2500;
		pwmVal2 =2500;

	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
	  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

	  if (a - 30 < 0)
	  {
		  a_done = 1;
		  b = b + (30 - a);
	  }

	  if (a_done == 1)
	  {
		  move_right_normal(185);
		  move_forced_b();
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
		  return;
		  //go B part
	  }

	  if ((distance + distance_b)/2 > (a-40))
	  {
		  a_done = 1;
	  }

	  osDelay(time_interval);
	}

}

/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */

  /* USER CODE END 1 */

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */

  //PID Controller Init
//  PID_Init(&TPID);
//  PID_Init(&TPID2);
//
//  PID(&TPID, &diff1, &pwmVal1, &pwmVal_set, 4, 3, 4,_PID_P_ON_E, _PID_CD_DIRECT);
//  PID2(&TPID2, &diff2, &pwmVal2, &pwmVal_set, 4, 3, 4, _PID_CD_DIRECT);
//  PID_SetSampleTime(&TPID, 20);
//  PID_SetOutputLimits(&TPID, 1, 10);


  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */

  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_TIM3_Init();
  MX_TIM8_Init();
  MX_TIM1_Init();
  MX_TIM2_Init();
  MX_ADC1_Init();
  MX_DMA_Init();
  MX_USART3_UART_Init();
  MX_I2C1_Init();
  /* USER CODE BEGIN 2 */
  OLED_Init();
  //OLED_Refresh_Gram();


  HAL_UART_Receive_IT(&huart3, (uint8_t *) aRxBuffer, 4 );

  HAL_TIM_Base_Start_IT(&htim1);

  ICM20948_init(&hi2c1, 0, GYRO_FULL_SCALE_2000DPS, ACCEL_FULL_SCALE_2G);


  /* USER CODE END 2 */

  /* USER CODE BEGIN RTOS_MUTEX */
  /* add mutexes, ... */
  /* USER CODE END RTOS_MUTEX */

  /* USER CODE BEGIN RTOS_SEMAPHORES */
  /* add semaphores, ... */
  /* USER CODE END RTOS_SEMAPHORES */

  /* USER CODE BEGIN RTOS_TIMERS */
  /* start timers, add new ones, ... */
  /* USER CODE END RTOS_TIMERS */

  /* USER CODE BEGIN RTOS_QUEUES */
  /* add queues, ... */
  /* USER CODE END RTOS_QUEUES */

  /* Create the thread(s) */
  /* definition and creation of defaultTask */
  osThreadDef(defaultTask, StartDefaultTask, osPriorityNormal, 0, 128);
  defaultTaskHandle = osThreadCreate(osThread(defaultTask), NULL);

  /* definition and creation of MotorTask */
  osThreadDef(MotorTask, Motor_Task, osPriorityIdle, 0, 128);
  MotorTaskHandle = osThreadCreate(osThread(MotorTask), NULL);

  /* definition and creation of EncoderTask */
  osThreadDef(EncoderTask, Encoder_Task, osPriorityIdle, 0, 128);
  EncoderTaskHandle = osThreadCreate(osThread(EncoderTask), NULL);

  /* definition and creation of Encoder_BTask */
  osThreadDef(Encoder_BTask, Encoder_Task, osPriorityIdle, 0, 128);
  Encoder_BTaskHandle = osThreadCreate(osThread(Encoder_BTask), NULL);

  /* definition and creation of ServoTask */
  osThreadDef(ServoTask, Servo_Task, osPriorityNormal, 0, 128);
  ServoTaskHandle = osThreadCreate(osThread(ServoTask), NULL);

  /* definition and creation of ShowTask */
  osThreadDef(ShowTask, Show_Task, osPriorityNormal, 0, 128);
  ShowTaskHandle = osThreadCreate(osThread(ShowTask), NULL);

  /* definition and creation of fakePID */
  osThreadDef(fakePID, fake_PID, osPriorityNormal, 0, 128);
  fakePIDHandle = osThreadCreate(osThread(fakePID), NULL);

  /* definition and creation of UltraSonic_Task */
  osThreadDef(UltraSonic_Task, US_Task, osPriorityNormal, 0, 128);
  UltraSonic_TaskHandle = osThreadCreate(osThread(UltraSonic_Task), NULL);

  /* definition and creation of DMA_ADC_Task */
  osThreadDef(DMA_ADC_Task, DMA_ADC_task, osPriorityIdle, 0, 128);
  DMA_ADC_TaskHandle = osThreadCreate(osThread(DMA_ADC_Task), NULL);

  /* definition and creation of ICM20948 */
  osThreadDef(ICM20948, ICM20948_Task, osPriorityNormal, 0, 128);
  ICM20948Handle = osThreadCreate(osThread(ICM20948), NULL);

  /* USER CODE BEGIN RTOS_THREADS */
  /* add threads, ... */
  /* USER CODE END RTOS_THREADS */

  /* Start scheduler */
  osKernelStart();

  /* We should never get here as control is now taken by the scheduler */
  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
  while (1)
  {
    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
  }
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  /** Configure the main internal regulator output voltage
  */
  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);
  /** Initializes the RCC Oscillators according to the specified parameters
  * in the RCC_OscInitTypeDef structure.
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.HSICalibrationValue = RCC_HSICALIBRATION_DEFAULT;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_NONE;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB buses clocks
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_HSI;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV1;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_0) != HAL_OK)
  {
    Error_Handler();
  }
}

/**
  * @brief ADC1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_ADC1_Init(void)
{

  /* USER CODE BEGIN ADC1_Init 0 */

  /* USER CODE END ADC1_Init 0 */

  ADC_ChannelConfTypeDef sConfig = {0};

  /* USER CODE BEGIN ADC1_Init 1 */

  /* USER CODE END ADC1_Init 1 */
  /** Configure the global features of the ADC (Clock, Resolution, Data Alignment and number of conversion)
  */
  hadc1.Instance = ADC1;
  hadc1.Init.ClockPrescaler = ADC_CLOCK_SYNC_PCLK_DIV2;
  hadc1.Init.Resolution = ADC_RESOLUTION_10B;
  hadc1.Init.ScanConvMode = ENABLE;
  hadc1.Init.ContinuousConvMode = ENABLE;
  hadc1.Init.DiscontinuousConvMode = DISABLE;
  hadc1.Init.ExternalTrigConvEdge = ADC_EXTERNALTRIGCONVEDGE_NONE;
  hadc1.Init.ExternalTrigConv = ADC_SOFTWARE_START;
  hadc1.Init.DataAlign = ADC_DATAALIGN_RIGHT;
  hadc1.Init.NbrOfConversion = 2;
  hadc1.Init.DMAContinuousRequests = DISABLE;
  hadc1.Init.EOCSelection = ADC_EOC_SINGLE_CONV;
  if (HAL_ADC_Init(&hadc1) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure for the selected ADC regular channel its corresponding rank in the sequencer and its sample time.
  */
  sConfig.Channel = ADC_CHANNEL_10;
  sConfig.Rank = 1;
  sConfig.SamplingTime = ADC_SAMPLETIME_28CYCLES;
  if (HAL_ADC_ConfigChannel(&hadc1, &sConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /** Configure for the selected ADC regular channel its corresponding rank in the sequencer and its sample time.
  */
  sConfig.Channel = ADC_CHANNEL_VBAT;
  sConfig.Rank = 2;
  sConfig.SamplingTime = ADC_SAMPLETIME_15CYCLES;
  if (HAL_ADC_ConfigChannel(&hadc1, &sConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN ADC1_Init 2 */

  /* USER CODE END ADC1_Init 2 */

}

/**
  * @brief I2C1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_I2C1_Init(void)
{

  /* USER CODE BEGIN I2C1_Init 0 */

  /* USER CODE END I2C1_Init 0 */

  /* USER CODE BEGIN I2C1_Init 1 */

  /* USER CODE END I2C1_Init 1 */
  hi2c1.Instance = I2C1;
  hi2c1.Init.ClockSpeed = 400000;
  hi2c1.Init.DutyCycle = I2C_DUTYCYCLE_2;
  hi2c1.Init.OwnAddress1 = 0;
  hi2c1.Init.AddressingMode = I2C_ADDRESSINGMODE_7BIT;
  hi2c1.Init.DualAddressMode = I2C_DUALADDRESS_DISABLE;
  hi2c1.Init.OwnAddress2 = 0;
  hi2c1.Init.GeneralCallMode = I2C_GENERALCALL_DISABLE;
  hi2c1.Init.NoStretchMode = I2C_NOSTRETCH_DISABLE;
  if (HAL_I2C_Init(&hi2c1) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN I2C1_Init 2 */

  /* USER CODE END I2C1_Init 2 */

}

/**
  * @brief TIM1 Initialization Function
  * @param None
  * @retval None
  */
static void MX_TIM1_Init(void)
{

  /* USER CODE BEGIN TIM1_Init 0 */

  /* USER CODE END TIM1_Init 0 */

  TIM_MasterConfigTypeDef sMasterConfig = {0};
  TIM_IC_InitTypeDef sConfigIC = {0};
  TIM_OC_InitTypeDef sConfigOC = {0};
  TIM_BreakDeadTimeConfigTypeDef sBreakDeadTimeConfig = {0};

  /* USER CODE BEGIN TIM1_Init 1 */

  /* USER CODE END TIM1_Init 1 */
  htim1.Instance = TIM1;
  htim1.Init.Prescaler = 160;
  htim1.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim1.Init.Period = 1000;
  htim1.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim1.Init.RepetitionCounter = 0;
  htim1.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;
  if (HAL_TIM_IC_Init(&htim1) != HAL_OK)
  {
    Error_Handler();
  }
  if (HAL_TIM_PWM_Init(&htim1) != HAL_OK)
  {
    Error_Handler();
  }
  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim1, &sMasterConfig) != HAL_OK)
  {
    Error_Handler();
  }
  sConfigIC.ICPolarity = TIM_INPUTCHANNELPOLARITY_BOTHEDGE;
  sConfigIC.ICSelection = TIM_ICSELECTION_DIRECTTI;
  sConfigIC.ICPrescaler = TIM_ICPSC_DIV1;
  sConfigIC.ICFilter = 0;
  if (HAL_TIM_IC_ConfigChannel(&htim1, &sConfigIC, TIM_CHANNEL_1) != HAL_OK)
  {
    Error_Handler();
  }
  sConfigOC.OCMode = TIM_OCMODE_PWM1;
  sConfigOC.Pulse = 0;
  sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
  sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;
  sConfigOC.OCIdleState = TIM_OCIDLESTATE_RESET;
  sConfigOC.OCNIdleState = TIM_OCNIDLESTATE_RESET;
  if (HAL_TIM_PWM_ConfigChannel(&htim1, &sConfigOC, TIM_CHANNEL_4) != HAL_OK)
  {
    Error_Handler();
  }
  sBreakDeadTimeConfig.OffStateRunMode = TIM_OSSR_DISABLE;
  sBreakDeadTimeConfig.OffStateIDLEMode = TIM_OSSI_DISABLE;
  sBreakDeadTimeConfig.LockLevel = TIM_LOCKLEVEL_OFF;
  sBreakDeadTimeConfig.DeadTime = 0;
  sBreakDeadTimeConfig.BreakState = TIM_BREAK_DISABLE;
  sBreakDeadTimeConfig.BreakPolarity = TIM_BREAKPOLARITY_HIGH;
  sBreakDeadTimeConfig.AutomaticOutput = TIM_AUTOMATICOUTPUT_DISABLE;
  if (HAL_TIMEx_ConfigBreakDeadTime(&htim1, &sBreakDeadTimeConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN TIM1_Init 2 */

  /* USER CODE END TIM1_Init 2 */
  HAL_TIM_MspPostInit(&htim1);

}

/**
  * @brief TIM2 Initialization Function
  * @param None
  * @retval None
  */
static void MX_TIM2_Init(void)
{

  /* USER CODE BEGIN TIM2_Init 0 */

  /* USER CODE END TIM2_Init 0 */

  TIM_Encoder_InitTypeDef sConfig = {0};
  TIM_MasterConfigTypeDef sMasterConfig = {0};

  /* USER CODE BEGIN TIM2_Init 1 */

  /* USER CODE END TIM2_Init 1 */
  htim2.Instance = TIM2;
  htim2.Init.Prescaler = 0;
  htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim2.Init.Period = 65535;
  htim2.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim2.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;
  sConfig.EncoderMode = TIM_ENCODERMODE_TI12;
  sConfig.IC1Polarity = TIM_ICPOLARITY_RISING;
  sConfig.IC1Selection = TIM_ICSELECTION_DIRECTTI;
  sConfig.IC1Prescaler = TIM_ICPSC_DIV1;
  sConfig.IC1Filter = 10;
  sConfig.IC2Polarity = TIM_ICPOLARITY_RISING;
  sConfig.IC2Selection = TIM_ICSELECTION_DIRECTTI;
  sConfig.IC2Prescaler = TIM_ICPSC_DIV1;
  sConfig.IC2Filter = 10;
  if (HAL_TIM_Encoder_Init(&htim2, &sConfig) != HAL_OK)
  {
    Error_Handler();
  }
  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim2, &sMasterConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN TIM2_Init 2 */

  /* USER CODE END TIM2_Init 2 */

}

/**
  * @brief TIM3 Initialization Function
  * @param None
  * @retval None
  */
static void MX_TIM3_Init(void)
{

  /* USER CODE BEGIN TIM3_Init 0 */

  /* USER CODE END TIM3_Init 0 */

  TIM_Encoder_InitTypeDef sConfig = {0};
  TIM_MasterConfigTypeDef sMasterConfig = {0};

  /* USER CODE BEGIN TIM3_Init 1 */

  /* USER CODE END TIM3_Init 1 */
  htim3.Instance = TIM3;
  htim3.Init.Prescaler = 0;
  htim3.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim3.Init.Period = 65535;
  htim3.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim3.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;
  sConfig.EncoderMode = TIM_ENCODERMODE_TI12;
  sConfig.IC1Polarity = TIM_ICPOLARITY_RISING;
  sConfig.IC1Selection = TIM_ICSELECTION_DIRECTTI;
  sConfig.IC1Prescaler = TIM_ICPSC_DIV1;
  sConfig.IC1Filter = 0;
  sConfig.IC2Polarity = TIM_ICPOLARITY_RISING;
  sConfig.IC2Selection = TIM_ICSELECTION_DIRECTTI;
  sConfig.IC2Prescaler = TIM_ICPSC_DIV1;
  sConfig.IC2Filter = 0;
  if (HAL_TIM_Encoder_Init(&htim3, &sConfig) != HAL_OK)
  {
    Error_Handler();
  }
  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim3, &sMasterConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN TIM3_Init 2 */

  /* USER CODE END TIM3_Init 2 */

}

/**
  * @brief TIM8 Initialization Function
  * @param None
  * @retval None
  */
static void MX_TIM8_Init(void)
{

  /* USER CODE BEGIN TIM8_Init 0 */

  /* USER CODE END TIM8_Init 0 */

  TIM_MasterConfigTypeDef sMasterConfig = {0};
  TIM_OC_InitTypeDef sConfigOC = {0};
  TIM_BreakDeadTimeConfigTypeDef sBreakDeadTimeConfig = {0};

  /* USER CODE BEGIN TIM8_Init 1 */

  /* USER CODE END TIM8_Init 1 */
  htim8.Instance = TIM8;
  htim8.Init.Prescaler = 0;
  htim8.Init.CounterMode = TIM_COUNTERMODE_UP;
  htim8.Init.Period = 7199;
  htim8.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
  htim8.Init.RepetitionCounter = 0;
  htim8.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;
  if (HAL_TIM_PWM_Init(&htim8) != HAL_OK)
  {
    Error_Handler();
  }
  sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
  sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
  if (HAL_TIMEx_MasterConfigSynchronization(&htim8, &sMasterConfig) != HAL_OK)
  {
    Error_Handler();
  }
  sConfigOC.OCMode = TIM_OCMODE_PWM1;
  sConfigOC.Pulse = 0;
  sConfigOC.OCPolarity = TIM_OCPOLARITY_HIGH;
  sConfigOC.OCNPolarity = TIM_OCNPOLARITY_HIGH;
  sConfigOC.OCFastMode = TIM_OCFAST_DISABLE;
  sConfigOC.OCIdleState = TIM_OCIDLESTATE_RESET;
  sConfigOC.OCNIdleState = TIM_OCNIDLESTATE_RESET;
  if (HAL_TIM_PWM_ConfigChannel(&htim8, &sConfigOC, TIM_CHANNEL_1) != HAL_OK)
  {
    Error_Handler();
  }
  if (HAL_TIM_PWM_ConfigChannel(&htim8, &sConfigOC, TIM_CHANNEL_2) != HAL_OK)
  {
    Error_Handler();
  }
  sBreakDeadTimeConfig.OffStateRunMode = TIM_OSSR_DISABLE;
  sBreakDeadTimeConfig.OffStateIDLEMode = TIM_OSSI_DISABLE;
  sBreakDeadTimeConfig.LockLevel = TIM_LOCKLEVEL_OFF;
  sBreakDeadTimeConfig.DeadTime = 0;
  sBreakDeadTimeConfig.BreakState = TIM_BREAK_DISABLE;
  sBreakDeadTimeConfig.BreakPolarity = TIM_BREAKPOLARITY_HIGH;
  sBreakDeadTimeConfig.AutomaticOutput = TIM_AUTOMATICOUTPUT_DISABLE;
  if (HAL_TIMEx_ConfigBreakDeadTime(&htim8, &sBreakDeadTimeConfig) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN TIM8_Init 2 */

  /* USER CODE END TIM8_Init 2 */

}

/**
  * @brief USART3 Initialization Function
  * @param None
  * @retval None
  */
static void MX_USART3_UART_Init(void)
{

  /* USER CODE BEGIN USART3_Init 0 */

  /* USER CODE END USART3_Init 0 */

  /* USER CODE BEGIN USART3_Init 1 */

  /* USER CODE END USART3_Init 1 */
  huart3.Instance = USART3;
  huart3.Init.BaudRate = 115200;
  huart3.Init.WordLength = UART_WORDLENGTH_8B;
  huart3.Init.StopBits = UART_STOPBITS_1;
  huart3.Init.Parity = UART_PARITY_NONE;
  huart3.Init.Mode = UART_MODE_TX_RX;
  huart3.Init.HwFlowCtl = UART_HWCONTROL_NONE;
  huart3.Init.OverSampling = UART_OVERSAMPLING_16;
  if (HAL_UART_Init(&huart3) != HAL_OK)
  {
    Error_Handler();
  }
  /* USER CODE BEGIN USART3_Init 2 */

  /* USER CODE END USART3_Init 2 */

}

/**
  * Enable DMA controller clock
  */
static void MX_DMA_Init(void)
{

  /* DMA controller clock enable */
  __HAL_RCC_DMA2_CLK_ENABLE();

  /* DMA interrupt init */
  /* DMA2_Stream0_IRQn interrupt configuration */
  HAL_NVIC_SetPriority(DMA2_Stream0_IRQn, 5, 0);
  HAL_NVIC_EnableIRQ(DMA2_Stream0_IRQn);

}

/**
  * @brief GPIO Initialization Function
  * @param None
  * @retval None
  */
static void MX_GPIO_Init(void)
{
  GPIO_InitTypeDef GPIO_InitStruct = {0};

  /* GPIO Ports Clock Enable */
  __HAL_RCC_GPIOE_CLK_ENABLE();
  __HAL_RCC_GPIOC_CLK_ENABLE();
  __HAL_RCC_GPIOA_CLK_ENABLE();
  __HAL_RCC_GPIOB_CLK_ENABLE();

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(GPIOE, OLED_SEL_Pin|OLEC_Pin|OLED_RST_Pin|OLED_DC_Pin
                          |LED3_Pin, GPIO_PIN_RESET);

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(GPIOA, AIN2_Pin|AIN1_Pin|BIN1_Pin|BIN2_Pin, GPIO_PIN_RESET);

  /*Configure GPIO pin Output Level */
  HAL_GPIO_WritePin(TRIG_GPIO_Port, TRIG_Pin, GPIO_PIN_RESET);

  /*Configure GPIO pins : OLED_SEL_Pin OLEC_Pin OLED_RST_Pin OLED_DC_Pin
                           LED3_Pin */
  GPIO_InitStruct.Pin = OLED_SEL_Pin|OLEC_Pin|OLED_RST_Pin|OLED_DC_Pin
                          |LED3_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
  HAL_GPIO_Init(GPIOE, &GPIO_InitStruct);

  /*Configure GPIO pins : AIN2_Pin AIN1_Pin BIN1_Pin BIN2_Pin */
  GPIO_InitStruct.Pin = AIN2_Pin|AIN1_Pin|BIN1_Pin|BIN2_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_VERY_HIGH;
  HAL_GPIO_Init(GPIOA, &GPIO_InitStruct);

  /*Configure GPIO pin : TRIG_Pin */
  GPIO_InitStruct.Pin = TRIG_Pin;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
  HAL_GPIO_Init(TRIG_GPIO_Port, &GPIO_InitStruct);

}

/* USER CODE BEGIN 4 */

void HAL_UART_RxCpltCallback(UART_HandleTypeDef *huart)
{

	//UNUSED(huart);

	//HAL_UART_Transmit(&huart3, (uint8_t * ) aRxBuffer, 4 , 0xFFFF);


	  //HAL_UART_Transmit(&huart3, (uint8_t * ) aRxBuffer, 4 , 0xFFFF);
	  number = 0;


	instruction = (char) aRxBuffer[0];
	  for (int i=1; i<4;i++)
	  {
		number = number * 10;
		number += (int)((char) aRxBuffer[i] - '0');
	  }

	  choice = 1;
	  HAL_UART_Receive_IT(&huart3, (uint8_t *) aRxBuffer, 4);
	  //osDelay(10);

}

void HAL_TIM_IC_CaptureCallback(TIM_HandleTypeDef *htim)
{
	if (htim->Channel == HAL_TIM_ACTIVE_CHANNEL_1)
	{
		if (first_capture ==0)
		{
			IC_Val1 = HAL_TIM_ReadCapturedValue(htim, TIM_CHANNEL_1);
			//IC_Val1 =htim->CCR1;
			first_capture = 1;
		}
		else
		{
			IC_Val2 = HAL_TIM_ReadCapturedValue(htim, TIM_CHANNEL_1);
			//IC_Val2 =htim->CCR1;
			__HAL_TIM_SET_COUNTER(htim, 0);

			if (IC_Val2<IC_Val1)
			{
				IC_Val2 = IC_Val2 +1000;
			}

			difference_US = IC_Val2 - IC_Val1;
			distance_US = difference_US/1000.0 * 340.0/2.0; //difference_US/(16MHz / 160 Prescaler) * 340 * 100 (convert to cm/s) / 2.0 (there and back)
			//first_capture = 0;
		}
	}
}

/* USER CODE END 4 */

/* USER CODE BEGIN Header_StartDefaultTask */
/**
  * @brief  Function implementing the defaultTask thread.
  * @param  argument: Not used
  * @retval None
  */
/* USER CODE END Header_StartDefaultTask */
void StartDefaultTask(void const * argument)
{
  /* USER CODE BEGIN 5 */
	/* Infinite loop */
	//uint8_t ch = "A";
	uint8_t hello[20];
	int repeat;

  for(;;)
  {
//	  if(ch<'Z')
//		  ch++;
//	  else ch = 'A';
	  //aRxBuffer
	  if (choice)
	  {
		 switch (instruction)
		  {
		  case 'f':
			  sprintf(hello, "%3d",number);
			  OLED_ShowString(10,50,hello);
			  move_forward(number);
			  //sprintf(hello, "%s","X");
			  //OLED_ShowString(10,50,hello);
			  choice = 0;
			  instruction = 'x';
			  HAL_UART_Transmit(&huart3, (uint8_t * ) DONE, 7 , 0xFFFF);
			  break;

		  case 'b':
			  sprintf(hello, "%3d",number);
			  OLED_ShowString(10,50,hello);
			  move_till_left(1);
			  choice = 0;
			  instruction = 'x';
			  HAL_UART_Transmit(&huart3, (uint8_t * ) DONE, 7 , 0xFFFF);
			  break;

		  case 'l':
			  sprintf(hello, "%3d",number);
			  OLED_ShowString(10,50,hello);

			  if (number == 360)
			  {
				  repeat = 4;
			  }
			  else if (number == 180)
			  {
				  repeat = 2;
			  }
			  else if (number == 270)
			  {
				  repeat = 3;
			  }
			  else
			  {
				  repeat = 1;
			  }

			  for (int i = 0; i<repeat; i++)
			  {
				  move_left_normal(80);
			  }

			  choice = 0;
			  instruction = 'x';
			  HAL_UART_Transmit(&huart3, (uint8_t * ) DONE, 7 , 0xFFFF);
			  break;

		  case 'r':

			  if (number == 360)
			  {
				  repeat = 4;
			  }
			  else if (number == 180)
			  {
				  repeat = 2;
			  }
			  else if (number == 270)
			  {
				  repeat = 3;
			  }
			  else
			  {
				  repeat = 1;
			  }

			  for (int i = 0; i<repeat; i++)
			  {
				  move_right_normal(90);
			  }
			  sprintf(hello, "%3d",number);
			  OLED_ShowString(10,50,hello);
			  //move_right(number);
			  choice = 0;
			  instruction = 'x';
			  HAL_UART_Transmit(&huart3, (uint8_t * ) DONE, 7 , 0xFFFF);
			  break;


		  case 't':				//Challenge 2
			  OLED_ShowString(10,50,"TEST");
			  move_till_left(more_than_150);
			  move_forward_till_turn();
			  move_left_normal(93);
			  move_forward_till_UR();
			  choice = 0;
			  instruction = 'x';
			  HAL_UART_Transmit(&huart3, (uint8_t * ) DONE, 7 , 0xFFFF);
			  break;

	  	case 'w':		//Real Challenge 2
	  		sprintf(hello, "%3d",number);
			  OLED_ShowString(10,50,hello);
			  move_till_left(more_than_150);
			  move_forced();
			  if (!c_lessthan10)
			  {
				  move_left_normal(88);
			  }
			  move_forward_till_UR();
			  //sprintf(hello, "%s","X");
			  //OLED_ShowString(10,50,hello);
			  choice = 0;
			  instruction = 'x';
			  HAL_UART_Transmit(&huart3, (uint8_t * ) DONE, 7 , 0xFFFF);
			  break;


		  default:
			  sprintf(hello, "%s","XXX");
			  OLED_ShowString(10,50,hello);
			  choice = 0;
			  instruction = 'x';
			  break;
		  }

	  }
	  osDelay(100);


	  //HAL_UART_Receive_IT(&huart3, (uint8_t *) aRxBuffer, 4);
  }
  /* USER CODE END 5 */
}

/* USER CODE BEGIN Header_Motor_Task */
/**
* @brief Function implementing the MotorTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_Motor_Task */
void Motor_Task(void const * argument)
{
  /* USER CODE BEGIN Motor_Task */

		int time_interval = 10;

		int distance_desired = 120;

		int counter = 0;

		distance = 0;

		pwmVal1 = 1000;
		pwmVal2 = 1000;
		HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_2);
		HAL_TIM_PWM_Start(&htim8, TIM_CHANNEL_1);

		uint8_t hello[20];

		osDelay(2000);

	  /* Infinite loop */
	  for(;;)
	  {
		  //pwnVal1 = 1000;
		  //pwnVal2 = 1000;


		  //HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_SET);
		  //HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_RESET);
		  //HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_SET);
		  //HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_RESET);

		  osDelay(time_interval);
		  counter++;

		  //__HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwmVal1); //Modify comparison value of the duty cycle
		  //__HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwmVal2);

//		  if (distance > distance_desired)
//		  {
//			  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, 0); //Modify comparison value of the duty cycle
//			  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, 0);
//			 osDelay(100);
//			 break;
//		  }


//		  sprintf(hello, "pwmVal1: %5d", (int) pwmVal1);
//		  OLED_ShowString(10,10,hello);
//		  sprintf(hello, "pwmVal2: %5d", (int) pwmVal2);
//		  OLED_ShowString(10,20,hello);
//
//		  pwnVal1 = 1000;
//		  pwnVal2 = 1000;
//		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwnVal1); //Modify comparison value of the duty cycle
//		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwnVal2);
//		  osDelay(5000);



//		  HAL_GPIO_WritePin(GPIOA, BIN2_Pin, GPIO_PIN_SET);
//		  HAL_GPIO_WritePin(GPIOA, BIN1_Pin, GPIO_PIN_RESET);
//		  HAL_GPIO_WritePin(GPIOA, AIN2_Pin, GPIO_PIN_SET);
//		  HAL_GPIO_WritePin(GPIOA, AIN1_Pin, GPIO_PIN_RESET);
//
//		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_2, pwnVal); //Modify comparison value of the duty cycle
//		  __HAL_TIM_SetCompare(&htim8, TIM_CHANNEL_1, pwnVal);
//
//		  osDelay(5000);

	  }
	  osDelay(1);
  /* USER CODE END Motor_Task */
}

/* USER CODE BEGIN Header_Encoder_Task */
/**
* @brief Function implementing the EncoderTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_Encoder_Task */
void Encoder_Task(void const * argument)
{
  /* USER CODE BEGIN Encoder_Task */
	/* Infinite loop */
		HAL_TIM_Encoder_Start(&htim3,TIM_CHANNEL_ALL);
		HAL_TIM_Encoder_Start(&htim2,TIM_CHANNEL_ALL);

		int cnt1_A,cnt2_A, cnt1_B , cnt2_B ;
		int total_diff = 0;
		double time_interval = 50;
		uint32_t tick;

		double distance_now, distance_now_B;

		cnt1_B = __HAL_TIM_GET_COUNTER(&htim3);
		cnt1_A = __HAL_TIM_GET_COUNTER(&htim2);
		tick = HAL_GetTick();


		uint8_t hello[20];

	  for(;;)
	  {
		  if (HAL_GetTick()-tick > time_interval)
		  {
			  //osDelay(time_interval);
			  cnt2_B = __HAL_TIM_GET_COUNTER(&htim3);
			  cnt2_A = __HAL_TIM_GET_COUNTER(&htim2);
			  if (__HAL_TIM_IS_TIM_COUNTING_DOWN(&htim3))
			  {
				  if (cnt2_B<cnt1_B){
					  diff2 = cnt1_B - cnt2_B;
				  	  }
				 else{
					  diff2 = (65535 - cnt2_B) + cnt1_B;
				  	  }
			  }
			  else
			  {
				  if (cnt2_B > cnt1_B){
					  diff2 = cnt2_B - cnt1_B;
				  	  }
				  else{
					  diff2 = (65535 - cnt1_B) + cnt2_B;
				  	 }
			  }
			  if (__HAL_TIM_IS_TIM_COUNTING_DOWN(&htim2))
			  {
				  if (cnt2_A<cnt1_A){
					  diff1 = cnt1_A - cnt2_A;}
				 else{
					  diff1 = (65535 - cnt2_A) + cnt1_A;}
			  }
			  else
			  {
				  if (cnt2_A > cnt1_A){
					  diff1 = cnt2_A - cnt1_A;}
				  else{
					  diff1 = (65535 - cnt1_A) + cnt2_A;}
			  }

			  //dir2 = __HAL_TIM_IS_TIM_COUNTING_DOWN(&htim3);
			  if (diff2>10000)
			  {
				  diff2 = 0;
			  }
			  if (diff1> 10000)
			  {
				  diff1 = 0;
			  }

			  //total_diff += diff2;

			  rpm_B = ((diff2 / 1320.0)/(time_interval/1000) * 60 ) * 0.65;
			  //rpm_B = diff2*(60000/time_interval)/1320;
			  //if (rpm_B > 2900) {rpm_B = 0;}
			  //sprintf(hello, "B RPM: %5d", (int) rpm_B);
			  //OLED_ShowString(10,10,hello);

			  //dir1 = __HAL_TIM_IS_TIM_COUNTING_DOWN(&htim2);

			  rpm_A = ((diff1 / 1320.0)/(time_interval/1000) * 60 ) * 0.65 ;
			  //rpm_A = diff1*(60000/time_interval)/1320;
			  //if (rpm_A > 2900) {rpm_A = 0;}
			  //sprintf(hello, "A RPM: %5d", (int) rpm_A);
			  //OLED_ShowString(10,30,hello);

			  speed_B = rpm_B * circumference / 60 ;
			  //sprintf(hello, "B Speed: %5d", (int) speed_B);
			  //OLED_ShowString(10,20,hello);

			  speed_A = rpm_A * circumference / 60 ;
//			  sprintf(hello, "A Speed: %5d", (int) speed_A);
//			  OLED_ShowString(10,10,hello);

			  distance_now = speed_A * (time_interval/1000);
			  distance += distance_now;
			  sprintf(hello, "Dist A: %5d", (int) distance);
			  //OLED_ShowString(10,10,hello);

			  distance_now_B = speed_B * (time_interval/1000);
			  distance_b += distance_now_B;
			  sprintf(hello, "Dist B: %5d", (int) distance_b);
			  OLED_ShowString(10,10,hello);




			  //OLED_Refresh_Gram();

			  cnt1_B = __HAL_TIM_GET_COUNTER(&htim3);
			  cnt1_A = __HAL_TIM_GET_COUNTER(&htim2);
			  tick = HAL_GetTick();

		  }
	  }
	  osDelay(1);
  /* USER CODE END Encoder_Task */
}

/* USER CODE BEGIN Header_Servo_Task */
/**
* @brief Function implementing the ServoTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_Servo_Task */
void Servo_Task(void const * argument)
{
  /* USER CODE BEGIN Servo_Task */
  /* Infinite loop */
	HAL_TIM_PWM_Start(&htim1, TIM_CHANNEL_4);

	//Straighten the servos

//	htim1.Instance->CCR4 =118;
//	osDelay(1000);
//	htim1.Instance->CCR4 =178;
//	osDelay(1000);
  for(;;)
  {
	  if (!isTurning)
	  {
		  htim1.Instance->CCR4 =center; //center
		  osDelay(100);
	  }

	  //htim1.Instance->CCR4 =100;	//extreme leftl090
	  osDelay(500);
  }
  /* USER CODE END Servo_Task */
}

/* USER CODE BEGIN Header_Show_Task */
/**
* @brief Function implementing the ShowTask thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_Show_Task */
void Show_Task(void const * argument)
{
  /* USER CODE BEGIN Show_Task */
  /* Infinite loop */
  for(;;)
  {
	  OLED_Refresh_Gram();
	  HAL_GPIO_TogglePin(LED3_GPIO_Port, LED3_Pin);

	  osDelay(100);
  }
  /* USER CODE END Show_Task */
}

/* USER CODE BEGIN Header_fake_PID */
/**
* @brief Function implementing the fakePID thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_fake_PID */
void fake_PID(void const * argument)
{
  /* USER CODE BEGIN fake_PID */
  /* Infinite loop */
  for(;;)
  {
	if (!isTurning){
	 if (diff1 < diff2)
	{
		 if (diff2 - diff1 < 50)
		 {
			 pwmVal1 = pwmVal1 - 2 ;
		 }
		 else
		 {
			 if (diff2 - diff1 < 100)
			 {
				 pwmVal1 = pwmVal1 - 8 ;
			 }
			 else
			 {
				 pwmVal1 = pwmVal1 - 15;
			 }
		 }

	}
	else
	{
		if (diff1 - diff2 < 50)
		 {
			 pwmVal1 = pwmVal1 + 2 ;
		 }
		 else
		 {
			 if (diff1 - diff2 < 100)
			 {
				pwmVal1 = pwmVal1 + 8 ;
			 }
			 else
			 {
				 pwmVal1 = pwmVal1 + 15;
			 }
		 }

	}
    osDelay(100);
	  }
	else
	{
		osDelay(10);
	}
  }
  /* USER CODE END fake_PID */
}

/* USER CODE BEGIN Header_US_Task */
/**
* @brief Function implementing the UltraSonic_Task thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_US_Task */
void US_Task(void const * argument)
{
  /* USER CODE BEGIN US_Task */
  /* Infinite loop */

	uint8_t hello[20];
  for(;;)
  {

	  HAL_GPIO_WritePin(TRIG_GPIO_Port, TRIG_Pin, GPIO_PIN_SET);
    osDelay(1);
    __HAL_TIM_ENABLE_IT(&htim1, TIM_IT_CC1);
    HAL_GPIO_WritePin(TRIG_GPIO_Port, TRIG_Pin, GPIO_PIN_RESET);
    HAL_TIM_IC_Start_IT(&htim1, TIM_CHANNEL_1);

    //__HAL_TIM_ENABLE_IT(&htim1, TIM_IT_CC1);

    sprintf(hello, "US: %5d", distance_US);
   // sprintf(hello, "US: %5d", IC_Val2);
    OLED_ShowString(10,30,hello);
//    sprintf(hello, "US: %5d", IC_Val1);
//        OLED_ShowString(10,50,hello);

    osDelay(50);
    first_capture = 0;
  }
  /* USER CODE END US_Task */
}

/* USER CODE BEGIN Header_DMA_ADC_task */
/**
* @brief Function implementing the DMA_ADC_Task thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_DMA_ADC_task */
void DMA_ADC_task(void const * argument)
{
  /* USER CODE BEGIN DMA_ADC_task */
  /* Infinite loop */

	uint8_t hello[20];
  for(;;)
  {
	HAL_ADC_Start_DMA(&hadc1, &distance_IR, 12);

//	if (distance_IR > 220)
//	{
//		distance_IR = 0;
//	}

	sprintf(hello, "IR Dist: %5d", (uint32_t) distance_IR);
	OLED_ShowString(10,20,hello);

    osDelay(500);
  }
  /* USER CODE END DMA_ADC_task */
}

/* USER CODE BEGIN Header_ICM20948_Task */
/**
* @brief Function implementing the ICM20948 thread.
* @param argument: Not used
* @retval None
*/
/* USER CODE END Header_ICM20948_Task */
void ICM20948_Task(void const * argument)
{
  /* USER CODE BEGIN ICM20948_Task */
  /* Infinite loop */

	uint8_t hello[20];
	ICM_angle = 0 ;
	ICM_angle_2 = 0;

  for(;;)
  {

	ICM20948_readGyroscope_allAxises(&hi2c1, 0, GYRO_FULL_SCALE_2000DPS, readings);

	if (readings[2] > 32768)
	{
		readings[2] = 65536 - readings[2];
	}

	ICM_angle = readings[2] + ICM_angle ;
	ICM_angle_2 = readings[2] + ICM_angle_2;


	sprintf(hello, " %5d", (uint32_t) ICM_angle);
	OLED_ShowString(10,50,hello);

	sprintf(hello, " R[2]: %5d", (uint32_t) readings[2]);
	OLED_ShowString(10,40,hello);

    osDelay(10);
  }
  /* USER CODE END ICM20948_Task */
}

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
  /* User can add his own implementation to report the HAL error return state */
  __disable_irq();
  while (1)
  {
  }
  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

