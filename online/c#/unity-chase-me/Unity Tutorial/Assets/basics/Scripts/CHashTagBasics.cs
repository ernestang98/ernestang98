using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CHashTagBasics : MonoBehaviour
{
    double mana = 15.5;
    float speed = 1.4f;
    int health = 100;
    string playerName = "Ernest";
    bool isDead = false;
    char oneChar = 'a';

    // single line comment

    /*
     * multi line comment
     */

    // if changed to `Private void Start()` then it will not work! (Dependent on your version of Unity)
    // More on Coroutines: https://gamedevbeginner.com/coroutines-in-unity-when-and-how-to-use-them/
    void Start()
    {
        /*
        Coroutine coroutine = StartCoroutine(Execute());
        StopCoroutine(coroutine);
        */
        Player player = new Player(100, 10, "Ernest");
        player.Name = "Ernest Ang";
        player.SetHealth(200);
        player.printInfo();
    }

    void testPrintAndLog(int val)
    {
        print("this is from PRINT");
        Debug.Log("this is from DEBUG.LOG");
        print("value is: " + val);
    } 

    // Switch() also available
    int demoConditionals(float val)
    {
        if (val == 1)
        {
            return 1;
        }
        else if (val == 2)
        {
            return 2;
        }
        else
        {
            return 0;
        }
    }

    IEnumerator Execute()
    {

        print("wait for 2 seconds");

        yield return new WaitForSeconds(2f);

        int a = 1;
        int b = 2;
        int c = 0;

        c = a + b;
        c = a * b;
        c = a - b;
        c = a / b;

        for (int i = 0; i < b; i++)
        {
            if (i == 0)
            {
                testPrintAndLog(demoConditionals(a));
            }
            else
            {
                testPrintAndLog(c);
            }
        }

        while (a == 0) testPrintAndLog(demoConditionals(a));
    }

}
