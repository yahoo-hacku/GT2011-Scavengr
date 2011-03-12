ActiveRecord::Base.include_root_in_json = false

module JsonSubmodels  
  def self.included(base)
    base.extend ClassMethods
  end
  
  module ClassMethods
    def json_include(names)
      define_method :as_json do |args = {}|
        args[:include] = names
        super(args)
      end
    end
  end
end

ActiveRecord::Base.send :include, JsonSubmodels