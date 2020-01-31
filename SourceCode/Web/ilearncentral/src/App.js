import React, {Component} from 'react';
import {BrowserRouter, Switch, Route} from 'react-router-dom'

import Navbar from './components/layout/Navbar'
import Dashboard from './components/dashboard/Dashboard'
import ProjectDetails from './components/courses/CourseDetails'
import SignIn from './components/auth/SignIn'
import SignUp from './components/auth/SignUp'
import AccountSelection from './components/auth/AccountSelection'
import CreateCourse from './components/courses/CreateCourse'
import SignUpBusiness from './components/auth/SignUpBusiness'

class App extends Component {
  render() {
  return (
    <BrowserRouter>
    <div className="App">
      <Navbar/>
      <Switch>
        <Route exact path='/' component={Dashboard}/>
        <Route path = '/course/:id' component={ProjectDetails} />
        <Route path = '/signin' component={SignIn} />
        <Route exact path = '/signup' component={AccountSelection} />
        <Route path = '/signupcenter' component={SignUpBusiness} />
        <Route path = '/signup/:type' component={SignUp}  />
        <Route path = '/newcourse' component={CreateCourse} />
      </Switch>
    </div>
    </BrowserRouter>
  );
  }
}

export default App;
