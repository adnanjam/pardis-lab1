//
import java.util.concurrent.locks.ReentrantLock;



/**
 * Authors: Adnan Jamil Ahsan, Hovig Manjikian
 * Date: 2021-09-07
 * Lab 1 - DD2443
 *
 * In this solution we have two threads trying to acquire 2 locks, but when the firs thread
 * has acquired the first lock the other lock will be acquired by the second thread. The second
 * thread on the other hand will be trying to acquire the first lock that the first thread has.
 * After 100ms both threads will release their locks and try to start again by acquiring first the
 * other lock this time. This process will be repeated indefinitely leading to a live-lock.
 *
 * A suitable tool for finding this kind of problems will be Java Pathfinder. We have already
 * experimented with this tool in the course Software Safety and security (DD2460). Java pathfinder
 * is a tool with the primary application in Model checking of concurrent programs, to find
 * defects such as data races and deadlocks.
 */
public class Exercise5 {

    public  static void main(String[] args) throws InterruptedException{

        Database db = new Database();

        Thread t1 = new Thread(db::addUser);
        Thread t2 = new Thread(db::removeUser);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}


class Database  implements Runnable {

    int users = 0;
    int orders = 0;
    ReentrantLock lock1 =  new ReentrantLock();
    ReentrantLock lock2 =  new ReentrantLock();


    public void run() {

    }


    public void addUser(){
        System.out.println(Thread.currentThread().getName() + " trying to add user...");
        lock1.lock();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lock2.lock();

        try{
            users++;
            System.out.println(Thread.currentThread().getName()  + " added a user!");
        }finally {
            lock2.unlock();
            lock1.unlock();
        }
    }

    public void removeUser(){
        System.out.println(Thread.currentThread().getName() + " trying to remove user...");
        lock2.lock();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lock1.lock();

        try{
            users++;
            System.out.println(Thread.currentThread().getName()  + " removed a user!");
        }finally {
            lock2.unlock();
            lock1.unlock();
        }
    }
}
