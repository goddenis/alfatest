package com.alfatest.aspect;

import com.alfatest.domain.Identifiable;
import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.repository.CrudRepository;

@Slf4j
@Aspect
public class EntityLoggingAspect {



    @Around("execution(* com.alfatest.jpa.*.save(..))")
    public Object ddd(ProceedingJoinPoint joinPoint) throws Throwable {
        if((joinPoint.getArgs()[0] instanceof Identifiable) && (joinPoint.getSignature().getDeclaringType() ==CrudRepository.class)){
            Identifiable identifiable = (Identifiable) joinPoint.getArgs()[0];
            if (identifiable.getId() == null ){
                logCreation(identifiable);
            } else {
                CrudRepository aThis = (CrudRepository) joinPoint.getThis();
                Object oldObject = aThis.findOne(identifiable.getId());
                logUpdate(oldObject,identifiable);
            }
        }
        return joinPoint.proceed();
    }

    private void logUpdate(Object oldObject, Object newObject) {
        DiffNode compare = ObjectDifferBuilder.buildDefault().compare(oldObject, newObject);
        final String[] message = new String[1];
        message[0]="";
        if (compare.getState() == DiffNode.State.CHANGED){
            compare.visit((node, visit) ->{
                final Object baseValue = node.canonicalGet(oldObject);
                final Object workingValue = node.canonicalGet(newObject);
                message[0] = message[0] +( node.getPath() + " changed from " + baseValue + " to " + workingValue);
            });
            log.info("Entity {} updated {} ",oldObject.getClass().getName() ,message[0]);
        }

    }

    private void logCreation(Object o) {
        log.info("Entity {} created ",o.toString());
    }
}
