package net.gouline.dagger2demo.di;

import net.gouline.dagger2demo.DemoApplication;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Component for {@link DemoApplication}.
 * <p/>
 * This acts as pairing between the module and injection targets, each of which
 * has to have a corresponding inject method in this component.
 * <p/>
 * Created by mgouline on 23/04/15.
 * Major refactoring to use AndroidInjector by clkim on 7/4/2017.
 *  reference https://google.github.io/dagger/android.html
 */
@Component(modules = {AndroidSupportInjectionModule.class, DemoActivityModule.class})
public interface DemoApplicationComponent {
    void doInject(DemoApplication demoApplication);
}
