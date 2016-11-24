var index = angular.module('indexPage', [ 'ngMaterial', 'ngMessages', 'md.data.table']);



index.controller('IndexController', ['$scope','$filter', 
                                     function($scope, $filter ,$timeout, $q){
	
	$scope.states        = loadAll();
	$scope.querySearch   = querySearch;
	$scope.searchTextChange   = searchTextChange;
	$scope.symptomsSelected = [];
	$scope.newState = newState;
	$scope.options = {
			rowSelection : true,
			multiSelect : true,
			autoSelect : true,
			decapitate : false,
			largeEditDialog : false,
			boundaryLinks : false,
			limitSelect : true,
			pageSelect : true
		};

    function newState(state) {
      alert("Sorry! You'll need to create a Constitution for " + state + " first!");
    }

    function querySearch (query) {
      var results = query ? $scope.states.filter( createFilterFor(query) ) : $scope.states,
          deferred;
      if ($scope.simulateQuery) {
        deferred = $q.defer();
        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
        return deferred.promise;
      } else {
        return results;
      }
    }

    function searchTextChange(text) {
    	console.log('Text changed to ' + text);
    }
    
    
    $scope.selectedItemChange = function(item) {
    	console.log('Item changed to ' + JSON.stringify(item));
        if(item)
        {
          //check if item is already selected
          if($filter('filter')($scope.symptomsSelected, function (symptom) {return symptom === item.display;})[0])
            {
        	  console.log('Item already selected. Will not add it again.');
            }
          else
            {
              //add id to object
        	  $scope.symptomsSelected.push(item.display);    
            }
          // clear search field
          $scope.searchText = '';
          $scope.selectedItem = undefined;
          
          //somehow blur the autocomplete focus
          //$mdAutocompleteCtrl.blur();
          document.getElementById("symptomAc").blur();
        }
      };
    
    $scope.symptomsSelectedRemove = function(symptom){
    	var index = $scope.symptomsSelected.indexOf(symptom);
    	$scope.symptomsSelected.splice(index, 1);   
    	
    };
    
    /**
     * Build `states` list of key/value pairs
     */
    function loadAll() {
    
    	var allStates = 'Abdominal pain, Blood in stool, Chest pain, Constipation, Cough, Diarrhea,\
    		Difficulty swallowing, Dizziness, Eye discomfort and redness, Foot pain or ankle pain,\
    		Foot swelling or leg swelling, Headaches, Heart palpitations, Hip pain, Knee pain, Low back pain,\
    		Nasal congestion, Nausea or vomiting, Neck pain, Numbness or tingling in hands,\
    		Pelvic pain: Female, Pelvic pain: Male, Shortness of breath, Shoulder pain, Sore throat,\
    		Urinary problems, Vision problems, Wheezing';
   

      return allStates.split(/, +/g).map( function (state) {
        return {
          value: state.toLowerCase(),
          display: state
        };
      });
    }

    /**
     * Create filter function for a query string
     */
    function createFilterFor(query) {
      var lowercaseQuery = angular.lowercase(query);

      return function filterFn(state) {
        return (state.value.indexOf(lowercaseQuery) === 0);
      };

    };
    
    $scope.submit = function(){
    	console.log($scope.selectedItem);
    }
		
}]);
