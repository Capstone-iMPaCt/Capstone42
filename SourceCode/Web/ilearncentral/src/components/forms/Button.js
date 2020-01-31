import React from 'react'

const Button = (props) => {
    return(
        <button 
            className={props.className}
            style= {props.style} 
            onClick= {props.action}>    
            {props.title} 
        </button>)
}

export default Button;