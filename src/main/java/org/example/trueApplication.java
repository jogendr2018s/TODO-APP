package org.example;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.example.dao.TaskDAO;
import org.example.resources.Endpoints;

public class trueApplication extends Application<trueConfiguration> {

    public static void main(final String[] args) throws Exception {
        new trueApplication().run(args);
    }



    @Override
    public void initialize(final Bootstrap<trueConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final trueConfiguration configuration,
                    final Environment environment) {
        final TaskDAO taskDAO = new TaskDAO(configuration.getDatabaseUrl(), configuration.getDatabaseUser(), configuration.getDatabasePassword());
        final Endpoints resource = new Endpoints(taskDAO);
        environment.jersey().register(resource);

    }

}
