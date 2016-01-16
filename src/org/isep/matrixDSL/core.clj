(ns org.isep.matrixDSL.core
  (:require [instaparse.core :as insta]))

(import org.isep.matrixDSL.java.PersistentVectorCompiler)
(def vectorParser
  (insta/parser
   "S = addsub space (addsub)*
    <addsub> = add | sub | vector
    add = addsub space <'+'> space vector
    sub = addsub space <'-'> space vector
    <space> = <#'\\s*'>
    vector = <'['> vsize <','> argument <']'>
    vsize = #'\\d+'
    argument= <'%'>#'[0-9]+'"))

(def vector (vectorParser "[3,%2] + [3,%1]"))

(defn compile-exp [class-name exp] 
  (let [compiled (.compileExpression (PersistentVectorCompiler.) exp class-name)
        cl (clojure.lang.DynamicClassLoader.)]
         (.defineClass cl class-name compiled nil))
  (fn [class-name] 
    (clojure.lang.Reflector/invokeStaticMethod name "run" 
                                               (into-array (int-array [1 2 3]) (int-array [4 5 7])))))

(def dsl (compile-exp "DSL" vector))
(type dsl)


(PersistentVectorCompiler/test (vectorParser "[3,%2] + [3,%2] + [3,%3] + [3,%4] - [3,%10]"))

(comment
	(def vector-arith
	  (insta/parser
	   "prog = expr space (<';'> space expr)*
	    <expr> = assig | addsub
	    assig = varname space <'='> space expr
	    <addsub> = multdiv | add | sub
	    <space> = <#'\\s*'>"))
	(vector-arith "a=[3,%1];b=a+[3,%2];b+a"))
