#include <iostream>
#include <cstring>
#include <algorithm>
#include <set>
using namespace std;

int main()
{
    char first_guard = 'A';
    char last_guard;
    cout << "Please input the last guard (from A to what): " << endl;
    cin >> last_guard;
    int distance = (int)last_guard - (int)first_guard;
    string pattern;
    cout << "Please input the \"see don't see\" pattern (e.g. XOXOOXX means \"skip see skip see see skip skip\"): " << endl;
    cin >> pattern;
    if (pattern.length()-1 != distance)
    {
        cout << "Incorrect length. " << endl;
        return 0;
    }
    if (count(pattern.begin(), pattern.end(), 'O') + count(pattern.begin(), pattern.end(), 'X') != pattern.length())
    {
        cout << "Incorrect input. (only 'O' and 'X' allowed)" << endl;
        return 0;
    }
    cout << "Computing..." << endl;
    string output = "";
    set<string> result;
    string new_pattern;
    for (int i = 0; i < pattern.length(); ++i)
    {
        output = "";
        new_pattern = "";
        string subpattern1 = pattern.substr(0, i);
        string subpattern2 = pattern.substr(i, pattern.length());
        new_pattern = subpattern2 + subpattern1;
        int j = i;
        do
        {
            if (new_pattern.at(j) == 'O')
            {
                output += char(65+j);
            }
            else if (new_pattern.at(j) == 'X')
            {
                // do nothing
            }
            else
            {
                cout << "I have no idea why I'm here. \n" << endl;
            }
            j++;
            j = j % pattern.length();
        } while (j != i);
        sort(output.begin(), output.end());
        //output += '\0';
        result.insert(output);
    }

    for (auto i = result.begin(); i != result.end(); i++)
    {
        cout << *i << endl;
    }
      

    return 0;
}