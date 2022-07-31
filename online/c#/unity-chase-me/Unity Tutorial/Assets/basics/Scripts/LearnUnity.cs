using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LearnUnity : MonoBehaviour
{
    private Rigidbody2D body;
    private BoxCollider2D collider;
    private Animator animate;
    private AudioSource audiosource;

    private Transform thistransform;

    // Start is called before the first frame update
    void Start()
    {
        body = GetComponent<Rigidbody2D>();

        // 2 ways:
        // second way works because we inherited monobehaviour
        thistransform = GetComponent<Transform>();
        thistransform = transform;

        // set body in ridiybody2d to kinematic
        thistransform.position = new Vector3(10, 10, 10);

    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
