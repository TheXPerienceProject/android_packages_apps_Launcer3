package com.google.research.reflection.layers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class e {
    static e Nt;
    public static boolean Nu;
    private int Ns;
    private ExecutorService Nv;
    private boolean Nw;
    private int Nx;

    static {
        e.Nu = false;
    }

    private e() {
        this.Nw = false;
        this.Ns = Runtime.getRuntime().availableProcessors() / 2;
        this.Nv = Executors.newFixedThreadPool(this.Ns);
    }

    private void Ud(final int n, final c c) {
        int i = 1;
        synchronized (this) {
            final boolean nw = true;
            this.Nw = nw;
            final ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(this.Nv);
            int n2;
            if (n >= this.Ns) {
                i = (n2 = (int) Math.ceil(n / this.Ns));
            } else {
                n2 = i;
            }
            i = this.Ns;
            i = Math.min(i, n);
            this.Nx = i;
            for (i = 0; i < this.Nx; ++i) {
                executorCompletionService.submit(new n(i, n2, n, c));
            }
            i = 0;
            while (i < this.Nx) {
                try {
                    final Future take = executorCompletionService.take();
                    take.get();
                    ++i;
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex2) {
                    System.err.println(new StringBuilder(48).append("threadCount: ").append(this.Nx).append(" for length: ").append(n).toString());
                    ex2.printStackTrace();
                }
            }
            this.Nw = false;
        }
    }

    public static e getInstance() {
        if (e.Nt == null) {
            e.Nt = new e();
        }
        return e.Nt;
    }

    public void Uc(final int n, final c c) throws InvalidValueException {
        int i = 0;
        if (e.Nu && !this.Nw && n != 1) {
            this.Ud(n, c);
        } else {
            while (i < n) {
                c.Ub(i);
                ++i;
            }
        }
    }

    static class n implements Callable {
        int Oq;
        c Or;
        private int Os;
        int Ot;

        public n(final int os, final int oq, final int ot, final c or) {
            this.Os = os;
            this.Oq = oq;
            this.Ot = ot;
            this.Or = or;
        }

        public Boolean call() throws InvalidValueException {
            for (int i = this.Oq * this.Os; i < Math.min(this.Ot, (this.Os + 1) * this.Oq); ++i) {
                this.Or.Ub(i);
            }
            return true;
        }
    }
}
