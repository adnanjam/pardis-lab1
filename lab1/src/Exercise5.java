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

    public  static void main(String[] args) {

        Database db = new Database();

            Thread t1 = new Thread(db::addUser);
            Thread t2 = new Thread(db::removeUser);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class Database  implements Runnable {

    int users = 0;
    int orders = 0;
    ReentrantLock lock1 =  new ReentrantLock();
    ReentrantLock lock2 =  new ReentrantLock();


    public void run() {

    }


    public void addUser() {
        // 1. Try to get lock 1
        // 2. Wait some time and then try to get Lock 2
        // 3. Release lock 1 if lock 2 cannot be acquired and try again

        while(true){


            System.out.println(Thread.currentThread().getName() + " trying to add user...");

            lock1.lock();
            chill(100);

            if(lock2.tryLock()){
                System.out.println(Thread.currentThread().getName() +" got lock 1");

            }else{
                lock1.unlock();
                continue;
            }

            users++;
            System.out.println(Thread.currentThread().getName()  + " added a user!");
            break;
        }

        lock2.unlock();
        lock1.unlock();

    }

    public void removeUser() {

        // 1. Try to get lock 2
        // 2. Wait some time and try to get lock 1
        // 3. Release lock 2 if lock 1 cannot be acquired and try again
        while(true){

            System.out.println(Thread.currentThread().getName() + " trying to remove user...");

            lock2.lock();
            chill(200);

            if(lock1.tryLock()){
                System.out.println(Thread.currentThread().getName() +" got lock 2");
            }else{
                lock2.unlock();
                continue;
            }

            users++;
            System.out.println(Thread.currentThread().getName()  + " remove a user!");
            break;
        }

        lock2.unlock();
        lock1.unlock();

    }

    public void chill(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
