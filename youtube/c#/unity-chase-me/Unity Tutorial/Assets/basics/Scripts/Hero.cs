using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Hero : Player
{
    public Hero(int health, int power, string name)
    {
        this.SetHealth(health);
        this.SetPower(power);
        this.Name = name;
    }

    public override void Attack()
    {
        Debug.Log("the warrior is attacking");
    }

}
