import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

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
