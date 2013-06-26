package com.ttpod.rest.anno;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.codehaus.groovy.transform.sc.StaticCompileTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * date: 13-6-4 下午4:36
 * @author: yangyang.cong@ttpod.com
 */
//@GroovyASTTransformation(phase= CompilePhase.SEMANTIC_ANALYSIS)
//@GroovyASTTransformation(phase= CompilePhase.CANONICALIZATION)
@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
public class RestStaticCompileProcessor extends StaticCompileTransformation {


    protected List<AnnotationNode> getTargetAnnotationList(String annotation) {
//
//        System.out.println(
//                "parent.getAnnotations" + parent.getAnnotations()
//        );
        List<AnnotationNode> list = new ArrayList<>();
        try {
            Class[] value = (Class[]) Class.forName(annotation).getDeclaredField("value").get(null);
            for(Class t : value){
                list.add( new AnnotationNode(ClassHelper.make(t)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



//        assert extensions != null;
//        System.out.println(
//                extensions.getType()
//        );
//        if (extensions instanceof ConstantExpression) {
//            list.add( new AnnotationNode(ClassHelper.make((Class) ((ConstantExpression) extensions).getValue())));
//        } else if (extensions instanceof ListExpression) {
//            ListExpression listExpr = (ListExpression) extensions;
//            for (Expression ext : listExpr.getExpressions()) {
//                list.add(new AnnotationNode(ClassHelper.make((Class) ((ConstantExpression) ext).getValue())));
//            }
//        }
        return list;
    }

    @Override
    public void visit(ASTNode[] nodes, SourceUnit source) {
        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        AnnotationNode anno = (AnnotationNode) nodes[0];


        if(parent instanceof ClassNode){
            ClassNode classNode = (ClassNode) parent;
            List<AnnotationNode>  list  = getTargetAnnotationList(anno.getClassNode().getName());
            classNode.addAnnotations(list);
//            System.out.println("Over");
        }

        super.visit(nodes,source);
    }

//    static {
//        try {
//            System.setOut(new PrintStream(new File("c:/debug.txt")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
