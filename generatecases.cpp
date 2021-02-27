#include<iostream>
#include<fstream>
#include<string>
using namespace std;

int main(){
    string vp1 = "ACEGH";
    string vp2 = "ABDFH";
    string vp3 = "ACFH";
    string vp4 = "ABDEGH";
    int index;
    int case_num = 1;
    string guards[10]= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    
    for (int i = 0; i < 12; ++i)
    {
        for (int j = 0; j < 12; ++j)
        {
            for (int k = 0; k < 12; ++k)
            {
                for (int l = 0; l < 12; ++l)
                {
                    if ((i != j) && (i != k) && (i != l) && (j != k) && (j != l) && (k != l)) 
                    {
                        // create a new file
                        fstream f;
                        string filename = "case" + to_string(case_num) + ".txt";
                        f.open(filename, ios::out);
                        case_num++;
                        index = 0;
                        for (int r = 0; r < 12; ++r)
                        {
                            if (r == i) { f << vp1 << endl; }
                            else if (r == j) { f << vp2 << endl; }
                            else if (r == k) { f << vp3 << endl; }
                            else if (r == l) { f << vp4 << endl; }
                            else { 
                                f << guards[index] << endl;
                                index++;
                            }
                        }
                        f.close();
                    }
                }
            }
        }
    }

	return 0;
}
