package com.solve.global.common.enums

enum class ProgrammingLanguage(val template: String) {
    JAVA(
        """
public class Main {
   public static void main(String[] args) {

   }
}
""".trimIndent()
    ),

    KOTLIN(
        """
fun main() {

}
""".trimIndent()
    ),

    PYTHON(
        """

""".trimIndent()
    ),

    C(
        """
#include <stdio.h>

int main() {

   return 0;
}
""".trimIndent()
    ),

    CPP(
        """
#include <iostream>
using namespace std;

int main() {

   return 0;
}
""".trimIndent()
    ),

    CSHARP(
        """
using System;

class MainClass {
   static void Main() {

   }
}
""".trimIndent()
    ),

    NODE_JS(
        """

""".trimIndent()
    ),

    SWIFT(
        """

""".trimIndent()
    );
}