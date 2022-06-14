<?php 
    // error_reporting(E_ERROR | E_PARSE);
    class Template {

        protected $template;

        private $data = array();

        public function __construct($template) {
            $this->template = $template;
        }

        public function __set($name, $value) {
            // echo "Setting '$name' to '$value'\n";
            $this->data[$name] = $value;
        }

        public function __get($name) {
            // echo "Getting '$name'\n";
            if (array_key_exists($name, $this->data)) {
                return $this->data[$name];
            }
            $trace = debug_backtrace();
            trigger_error(
                'Undefined property via __get(): ' . $name .
                ' in ' . $trace[0]['file'] .
                ' on line ' . $trace[0]['line'],
                E_USER_NOTICE);
            return null;
        }

        public function __toString() {
            extract($this->data);
            $unstripped = dirname($this->template);
            $stripped = ltrim($unstripped,'/');
            chdir($stripped);
            ob_start();
            include basename($this->template);
            $out = ob_get_clean();
            return $out;
        }
    }