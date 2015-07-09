#ifndef _PHONEINFO_
#define _PHONEINFO_

#include <string.h>

//find the max length of the city name
//when write the city name on the file,set every city name's length to the "MaxCityLength"
//so when we try to find city,can quickly find the result like finding result in an array
const int MaxCityLength = 25;

//data like
///1315160,cityName1
///1315161,cityName1
///1315162,cityName2
///1315163,cityName2
///1315164,cityName2
///1315165,cityName2
///1315166,cityName2
//will compress to 13151,160,0 and 13151,462,1
//
//
class NumberInfoCompress{

public:
	NumberInfoCompress():m_before(0),m_after(0),m_cityIndex(0){
	}
	NumberInfoCompress(int begin,unsigned short skip,unsigned short cityIndex)
	{
		setBegin(begin);
		setSkip(skip);
		m_cityIndex = cityIndex;
	}
	int getBegin(){
		int lastTwoNumInAfter = m_after - getNumExceptLastTwo() * 100;
		return m_before * 100 + lastTwoNumInAfter; 
	}
	unsigned short getNumExceptLastTwo(){return m_after * 0.01;}
	unsigned short getSkip(){ return getNumExceptLastTwo(); }
	unsigned short getCityIndex()const{ return m_cityIndex; }
	unsigned short getLastTwoNum(int number){
		int exceptLastTwoNum = number * 0.01;
		return (number - exceptLastTwoNum * 100);
	}
	void setBegin(int& number){
		int lastTwoNum = getLastTwoNum(number);
		m_before = number * 0.01;
		m_after = getSkip() * 100 + lastTwoNum;
	}
	void setSkip(unsigned short skip){
	  m_after =	skip * 100 + getLastTwoNum(m_after);
	}
	void setCityIndex(unsigned short& city){m_cityIndex = city;}
private:
	unsigned short m_before;//store the 5 digit of the number,example: it store 136700 of 1367002
	

	unsigned short m_after;//store skip and last two digit of the number,
                        //example:102,means 1 is the skip,02 is the last two digit of the number(1367002)
	unsigned short  m_cityIndex;

};

class NumberInfoAction{
private:

char* DoFindResultThing( FILE* file,const int& phoneInfoCompressCount,const NumberInfoCompress &infoMiddle ) 
	{
		int totalOffset = sizeof(int) + phoneInfoCompressCount*sizeof(NumberInfoCompress) + infoMiddle.getCityIndex() * MaxCityLength;
	   //put the read point at the result
	   fseek(file,totalOffset,SEEK_SET);
	   char* location = new char[MaxCityLength];
       fread(location,MaxCityLength,1,file);
	   fclose(file);
	   return location;
	}
public:
	NumberInfoAction(){
	}
	~NumberInfoAction(){
	}

	// -------------------------------------------------------
	//  Name:         GetCityNameByNumber
	//  Description:  input the phone number, find the city in the binary file 
	//  Arguments:    bFileName:the binary file name,number: the phone number
	//  Return Value: city name,not find return ""
	// -------------------------------------------------------
	char* GetCityNameByNumber(const char* bFileName,const int& number){
        FILE* file = 0;
		file = fopen(bFileName,"rb");
		if(file == 0)
			return (char*)"";

		int phoneInfoCompressCount = 0;
		//get total phoneInfoCompress count
		fread(&phoneInfoCompressCount,sizeof(int),1,file);

		int left = 0, right = phoneInfoCompressCount - 1;
		NumberInfoCompress infoMiddle;
		//begin binary search
		while(left <= right){
			int middle = (left + right) / 2;
			//put the write point in the  middle phoneInfoCompress 
			fseek(file,sizeof(int) + middle * sizeof(NumberInfoCompress),SEEK_SET);		
			fread(&infoMiddle,sizeof(NumberInfoCompress),1,file);
			
			if(number < infoMiddle.getBegin()){
				right = middle - 1;
			}else if(number > (infoMiddle.getBegin() + infoMiddle.getSkip())){
				left = middle + 1;
			}else{// find the result
				return DoFindResultThing(file, phoneInfoCompressCount, infoMiddle);
			}
		}
        fclose(file);
		return (char*)"";
	}

};

#endif