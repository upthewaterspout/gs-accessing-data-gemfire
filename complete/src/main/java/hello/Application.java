package hello;

import java.io.IOException;
import java.util.Collection;

import org.apache.geode.cache.lucene.LuceneQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import org.apache.geode.cache.lucene.LuceneService;

public class Application implements CommandLineRunner {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    LuceneService luceneService;

    @Override
    public void run(String... strings) throws Exception {
        Person alice = new Person("Alice", 40);
        Person bob = new Person("Baby Bob", 1);
        Person carol = new Person("Teen Carol", 13);

        System.out.println("Before linking up with Gemfire...");
        for (Person person : new Person[] { alice, bob, carol }) {
            System.out.println("\t" + person);
        }

        personRepository.save(alice);
        personRepository.save(bob);
        personRepository.save(carol);

        final LuceneQuery<Object, Object> query =
            luceneService.createLuceneQueryFactory().create("luceneIndex", "hello", "bob", "name");

        final Collection<Object> bobs = query.findValues();
        System.out.println("Bobs are:" + bobs);
    }

    public static void main(String[] args) throws IOException {

        SpringApplication application = new SpringApplication(Application.class, ApplicationConfiguration.class);

        //Uncomment this line if you want to test with the xml configuration
        //SpringApplication application = new SpringApplication(new ClassPathResource("hello/cache-config.xml"), Application.class);

        application.setWebEnvironment(false);
        application.run(args);
    }

}
