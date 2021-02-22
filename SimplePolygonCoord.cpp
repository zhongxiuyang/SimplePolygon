#include <iostream>
#include <cmath>
#include <cstring>
#include <fstream>
#include <iomanip>
using namespace std;

string guards("ABCDEFGH");
long double radius(5.0);

void checkArray(string points[], int guardIndex[], long double x[], long double y[], int size, long double r);
bool SeeEachOther(string points[], long double x[], long double y[], int size, long double r, int AIndex, int BIndex);
bool ASeesB(string points[], long double x[], long double y[], int size, long double r, int AIndex, int BIndex);
bool doesIntersect(long double x1, long double y1, long double x2, long double y2, long double x3, long double y3, long double x4, long double y4);
bool doesIntersectInRay(long double x1, long double y1, long double x2, long double y2, long double x3, long double y3, long double x4, long double y4);
bool CBelowAB(long double Ax, long double Ay, long double Bx, long double By, long double Cx, long double Cy);
long double distance(long double Ax, long double Ay, long double Bx, long double By);
void printInput(string points[], long double x[], long double y[], int size);

int main()
{
	fstream f;
	string value;
	string filename = "./newinput.txt";
    // input is clockwised
	
	ifstream myFile;
	string line;
	int lines;
	myFile.open(filename);
	for(lines = 0; getline(myFile,line); lines++);
	myFile.close();

	int size = lines;
	string* points = new string[size]; // point or guard name
	long double* x = new long double[size]; // x-coords
	long double* y = new long double[size]; // y-coords
    int *gIndex = new int[guards.length()];
	f.open(filename.c_str());
	int i = 0; 
	int j = 0;
	
	while (f >> value)
	{
		if (i % 3 == 0)
		{
			points[j] = value;
            if (points[j].length() == 1) // save guard index to gIndex
            {
                int index = (int)(points[j].at(0));
                gIndex[(index - 65)] = j;
            }
		}
		if (i % 3 == 1)
		{
			x[j] = atof(value.c_str());
		}
		if (i % 3 == 2)
		{
			y[j] = atof(value.c_str());
			j++;
		}
		i++;
	}
	cout << showpoint;
	cout << setprecision(12);

	printInput(points, x, y, size);
    checkArray(points, gIndex, x, y, size, radius);
	return 0;
}

void checkArray(string points[], int guardIndex[], long double x[], long double y[], int size, long double r)
{
    for (int i = 0; i < size; ++i)
    {
        if (points[i].length() > 1) // viewpoint
		{
            for (int j = 0; j < guards.length(); ++j)
			{
                if (strchr(points[i].c_str(), guards.at(j)))
                {
                    if (!SeeEachOther(points, x, y, size, r, i, guardIndex[j]) )
                    {
                        cout << "Error: " << points[i] << " does not see " << points[guardIndex[j]] << endl;
                    }
                }
                else
                {
                    if (SeeEachOther(points, x, y, size, r, i, guardIndex[j]))
                    {
                        cout << "Error: " << points[i] << " sees " << points[guardIndex[j]] << endl;
                    }
                }
            }
        }
    }
}

bool SeeEachOther(string points[], long double x[], long double y[], int size, long double r, int AIndex, int BIndex)
{
    if (distance(x[AIndex], y[AIndex], x[BIndex], y[BIndex]) > r)
    {
        return false; 
    }
    if (!ASeesB(points, x, y, size, r, AIndex, BIndex))
    {
        return false;
    }
    if (!ASeesB(points, x, y, size, r, BIndex, AIndex))
    {
        return false;
    }
    return true;   
}

bool ASeesB(string points[], long double x[], long double y[], int size, long double r, int AIndex, int BIndex)
{
    if (((AIndex + 1)%size == BIndex) || ((BIndex + 1)%size == AIndex))
    {
        return true;
    }
    if (AIndex < BIndex)
    {
        for (int i = (AIndex + 1); i < (BIndex - 1); i++)
        {
            if (doesIntersect(x[i], y[i], x[i+1], y[i+1], x[AIndex], y[AIndex], x[BIndex], y[BIndex]))
            {
                return false;
            }
        }
    }
    if (AIndex > BIndex)
    {
        for (int i = (AIndex + 1); i < (BIndex + size - 1); i++)
        {
            if (doesIntersect(x[(i%size)], y[(i%size)], x[(i+1)%size], y[(i+1)%size], x[AIndex], y[AIndex], x[BIndex], y[BIndex]))
            {
                return false;
            }
        }
    }
    
    long double x_midpoint = (x[AIndex] + x[BIndex]) / 2.0;
    long double y_midpoint = (y[AIndex] + y[BIndex]) / 2.0;
    
    int intersects = 0;
    for (int i = AIndex; i < (AIndex +size); i++)
    {
        if (doesIntersectInRay(x[i%size], y[i%size], x[(i+1)%size], y[(i+1)%size], x_midpoint, y_midpoint, x_midpoint+1, y_midpoint-1))
        {
            intersects++;
        }
    }
    return (intersects % 2 == 1);
}

bool doesIntersectInRay(long double x1, long double y1, long double x2, long double y2, long double x3, long double y3, long double x4, long double y4)
{
    // Line AB represented as a1x + b1y = c1 
    long double a1 = y2 - y1; 
    long double b1 = x1 - x2; 
    long double c1 = a1*(x1) + b1*(y1); 
  
    // Line CD represented as a2x + b2y = c2 
    long double a2 = y4 - y3; 
    long double b2 = x3 - x4; 
    long double c2 = a2*(x3)+ b2*(y3); 
  
    long double determinant = a1*b2 - a2*b1; 
  
    if (determinant != 0)
    { 
        long double x = (b2*c1 - b1*c2)/determinant; 
        long double y = (a1*c2 - a2*c1)/determinant; 
        if (((x > x1) && (x < x2)) || ((x > x2) && (x < x1)))
        {
            if ((y < y3) && (x > x3))
            {
                return true;
            }
        } 
    } 
    return false;
}

bool doesIntersect(long double x1, long double y1, long double x2, long double y2, long double x3, long double y3, long double x4, long double y4)
{
    // Line AB represented as a1x + b1y = c1 
    long double a1 = y2 - y1; 
    long double b1 = x1 - x2; 
    long double c1 = a1*(x1) + b1*(y1); 
  
    // Line CD represented as a2x + b2y = c2 
    long double a2 = y4 - y3; 
    long double b2 = x3 - x4; 
    long double c2 = a2*(x3)+ b2*(y3); 
  
    long double determinant = a1*b2 - a2*b1; 
  
    if (determinant != 0)
    { 
        long double x = (b2*c1 - b1*c2)/determinant; 
        long double y = (a1*c2 - a2*c1)/determinant; 
        if (((x > x3) && (x < x4)) || ((x > x4) && (x < x3)))
        {
            if (((x > x1) && (x < x2)) || ((x > x2) && (x < x1)))
            {
                return true;
            }
        } 
    } 
    return false;
}

bool CBelowAB(long double Ax, long double Ay, long double Bx, long double By, long double Cx, long double Cy)
{
    long double m = (Ay - By) / (Ax - Bx);
	long double b = Ay - m * Ax;
	long double y = m * Cx + b;
	return (y >= Cy);
}

long double distance(long double Ax, long double Ay, long double Bx, long double By)
{
	return sqrt(pow((Bx - Ax), 2) + pow((By - Ay), 2));
}

void printInput(string points[], long double x[], long double y[], int size)
{
	for (int i = 0; i < size; ++i)
	{
		cout << points[i] << "(" << x[i] << ", " << y[i] << ")" << endl;
	}
}
