(ns bog.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [bog.core-test]
   [bog.common-test]))

(enable-console-print!)

(doo-tests 'bog.core-test
           'bog.common-test)
