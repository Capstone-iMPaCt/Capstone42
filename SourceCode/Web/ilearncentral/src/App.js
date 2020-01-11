import React, {Component} from 'react';
import {BrowserRouter, Switch, Route} from 'react-router-dom'

import Navbar from './components/layout/Navbar'
import Dashboard from './components/dashboard/Dashboard'
import ProjectDetails from './components/courses/CourseDetails'
import SignIn from './components/auth/SignIn'
import SignUp from './components/auth/SignUp'
import CreateCourse from './components/courses/CreateCourse'

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
        <Route path = '/signup' component={SignUp} />
        <Route path = '/newcourse' component={CreateCourse} />
      </Switch>
    </div>
    </BrowserRouter>
  );
  }
}

export default App;
