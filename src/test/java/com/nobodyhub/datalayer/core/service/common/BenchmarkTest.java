package com.nobodyhub.datalayer.core.service.common;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author yan_h
 * @since 2018-04-27.
 */
@State(Scope.Thread)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public abstract class BenchmarkTest {
    @Test
    public void benchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(getClass().getSimpleName())
                .warmupIterations(5)
                .measurementIterations(10)
                .forks(1)
                .build();
        Collection<RunResult> runResults = new Runner(opt).run();
        System.out.println(runResults);
    }
}
