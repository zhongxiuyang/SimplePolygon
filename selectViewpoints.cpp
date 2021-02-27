#include<iostream>
#include<string>
using namespace std;

int main(){

	int bits[10];
	for(int i=0; i<8; i++)
		bits[i] = 0;

	bits[9] = bits[8] = 1;
	bool finished = false;
	do{
		string thisVP;
		for(int i=0; i<10; i++){
			if(bits[i] == 1){
				
				char c = 'A' + i;
				thisVP += c;
			}
		}

		// if(thisVP.size() > 1)
        // {
        //     if(thisVP.find("B") != -1)  // 2 * 2^4 = 32
        //     {
        //         if(thisVP.find("G") != -1)
        //         {
        //             int count = 0;
        //             if(thisVP.find("A") != -1) // sees A
        //             {
        //                 count += 1;
        //             }
        //             if(thisVP.find("H") != -1)
        //             {
        //                 count += 1;
        //             }
        //             if (count == 1)
        //             {
        //                 cout << thisVP << endl;
        //             }
        //         }
        //     }
        //     if(thisVP.find("A") != -1)  // 2 * 2^4 = 32
        //     {
        //         if(thisVP.find("H") != -1)
        //         {
        //             cout << thisVP << endl;
        //         }
        //     }
        // }
        int degree = 0;
        
        if (thisVP.length() == 6)
        {
            degree = 0;
            for (int k = 0; k < 5; ++k)
            {
                degree += (int(thisVP.at(k+1)) - int(thisVP.at(k)));
            }
            if (degree > 8)
            {
                cout << thisVP << endl;
            }
        }
		int j=9;
		while(j>=0 && bits[j] == 1){
			bits[j] = 0;
			j--;
		}

		if(j>=0)
			bits[j] = 1;
		else
			finished = true;


	}while(!finished);


	return 0;
}
