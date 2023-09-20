import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
const onDeleteClick = (id) => {
    axios
      .delete(`http://localhost:8082/api/items/${id}`)
      .then((res) => {
        useNavigate('/');
      })
      .catch((err) => {
        console.log('Error form SecondPage_deleteClick');
      });
};
function Card({item, edit}) {
    if (edit) {
        return (
            <div className="Card" style={{
                backgroundImage: `url("` + item.img + `")`
            }}>
                <div className="controls">
                    <Link
                    className="controller material-symbols-outlined" to={`/EditPage/${item._id}`}>
                        edit
                    </Link>
                    <div
                    className="controller material-symbols-outlined" style={{backgroundColor: "#ff0010"}}
                    to={`/`}
                    onClick={() => {
                        onDeleteClick(item._id);
                        window.location.reload();
                      }}>
                        delete
                    </div>
                </div>
                <div className="details">
                    <div className="name">
                        <h1>{item.name}</h1>
                    </div>
                    <div className="description">
                        <p>{item.description}</p>
                    </div>
                </div>
            </div>
        );
    } else {
        return (
            <div className="Card" style={{
                backgroundImage: `url("` + item.img + `")`
            }}>
                <div className="details">
                    <div className="name">
                        <h1>{item.name}</h1>
                    </div>
                    <div className="description">
                        <p>{item.description}</p>
                    </div>
                </div>
            </div>
        );
    }
}
export default Card;