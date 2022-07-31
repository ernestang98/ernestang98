using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player
{

    private int power;

    public void SetPower(int health)
    {
        this.health = health;
    }

    public int GetPower()
    {
        return power;
    }

    private int health;

    public void SetHealth(int health)
    {
        this.health = health;
    }

    public int GetHealth()
    {
        return health;
    }

    private string _name;

    public string Name
    {
        get
        {
            return _name;
        }
        set
        {
            _name = value;
        }
    }

    public Player() { }

    public Player(int health, int power, string name)
    {
        Debug.Log("Creating player... ");
        this.health = health;
        this.power = power;
        Name = name;
    }

    public void printInfo()
    {
        Debug.Log("Health is: " + health);
        Debug.Log("Power is: " + power);
        Debug.Log("Name is: " + Name);
    }

    // add virtual to allow override
    public virtual void Attack()
    {
        Debug.Log(Name + " is attacking...");
    }

}
